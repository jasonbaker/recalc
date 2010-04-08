(ns test_parse
  (:use clojure.test
        recalc.parse))

(deftest num_test
  (is (= (:content (parse int-lit "12")) 12))
  (is (= (:content (parse float-lit "12.2")) 12.2))
  (is (= (:content (parse number "12")) 12))
  (is (= (:content (parse number "12.2")) 12.2)))
  
(deftest letter_test
  (is (= (parse letter "a") \a))
  (is (= (:content (parse identifier "abc123")) "abc123"))
  (is (= (:kind (parse identifier "abc123")) :variable)))