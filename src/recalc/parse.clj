(ns recalc.parse
  (:use name.choi.joshua.fnparse
        clojure.contrib.error-kit))

; A recalc node that will be returned
(defstruct node-s :kind :content)

; The parsing state
(defstruct state-s :remainder :column :line)

(def remainder-a
     (accessor state-s :remainder))

(def make-node
     (partial struct node-s))

(def make-scalar-node
     (partial make-node :scalar))

(def make-query-node
     (partial make-node :query))

(def apply-str
     (partial apply str))

;; These two functions are given a rule and make it so that it
;; increments the current column (or the current line).

(defn- nb-char [subrule]
  (invisi-conc subrule (update-info :column inc)))

(def nb-char-lit
  (comp nb-char lit))

(defn- b-char [subrule]
  (invisi-conc subrule (update-info :line inc)))


(def false-lit
     (constant-semantics (lit-conc-seq "false" nb-char-lit)
                         (make-scalar-node false)))

(def true-lit
     (constant-semantics (lit-conc-seq "true" nb-char-lit)
                         (make-scalar-node true)))

(def decimal-point
     (nb-char-lit \.))

(def digit
     (lit-alt-seq "0123456789" nb-char-lit))

(def apply-str (partial apply str))

(def int-lit
     (complex [num (rep* digit)]
              (-> num
                  (apply-str)
                  Integer/parseInt)))
            
(def float-lit
     (complex [whole int-lit
               _ decimal-point
               real int-lit]
              (-> [whole "." real]
                  (apply-str)
                  Double/parseDouble)))

(defn parse [rule tokens]
  (rule-match rule
              #(println "FAILED:  " %)
              #(println "LEFT OVER:  " %2)
              (struct state-s tokens 0 0 )))