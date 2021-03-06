(ns recalc.parse
  (:use name.choi.joshua.fnparse
        clojure.contrib.error-kit
        recalc.util))

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

(def make-projection-node
     (partial make-node :projection))

(def make-variable-node 
     (partial make-node :variable))

(def make-operator-node
     (partial make-node :operator))

;; These two functions are given a rule and make it so that it
;; increments the current column (or the current line).

(defn- nb-char [subrule]
  (invisi-conc subrule (update-info :column inc)))

(def nb-char-lit
  (comp nb-char lit))

(defn- b-char [subrule]
  (invisi-conc subrule (update-info :line inc)))

;; Terminal tokens

(def space (nb-char-lit \space))

(def tab (nb-char-lit \tab))

(def newline-lit (lit \newline))

(def return-lit (lit \return))

(def line-break (b-char (rep+ (alt newline-lit return-lit))))

(def ws (constant-semantics (rep* (alt space tab line-break)) :ws))

(def pipe
     (nb-char-lit "|"))

(def false-lit
     (constant-semantics (lit-conc-seq "false" nb-char-lit)
                         (make-scalar-node false)))

(def true-lit
     (constant-semantics (lit-conc-seq "true" nb-char-lit)
                         (make-scalar-node true)))

(def lcase-letter
     (lit-alt-seq
      "abcdefghijklmnopqrstuvwxyz" nb-char-lit))

(def ucase-letter
     (lit-alt-seq
      "ABCDEFGHIJKLMNOPQRSTUVWXYZ" nb-char-lit))

(def letter
     (alt lcase-letter ucase-letter))

(def uscore
     (nb-char-lit "_"))

(def digit
     (lit-alt-seq "0123456789" nb-char-lit))

;; Nonterminals

(def identifier
     (complex [_ (rep* ws)
               fchar (alt letter uscore)
               others (rep* (alt letter uscore digit))
               _ (rep* ws)]
              (->> [ fchar others ]
                   (apply cons)
                   (apply str)
		   make-variable-node)))

(def decimal-point
     (nb-char-lit \.))



(def int-lit
     (complex [num (rep* digit)]
              (-> num
                  (apply-str)
                  Integer/parseInt
		  make-scalar-node)))
            
(def float-lit
     (complex [whole int-lit
               _ decimal-point
               real int-lit]
              (-> [(:content whole) "." (:content real)]
                  (apply-str)
                  Double/parseDouble
		  make-scalar-node)))

(def number 
     (alt float-lit int-lit))

(def projection
     (complex [first-id identifier 
               _ ws
               other-ids (rep* identifier) ]
              (make-projection-node 
               (map :content (cons first-id other-ids)))))

(def expression
     (alt identifier number))

(defn parse [rule tokens]
  (rule-match rule
              #(println "FAILED:  " %)
              #(println "LEFT OVER:  " %2)
              (struct state-s tokens 0 0 )))