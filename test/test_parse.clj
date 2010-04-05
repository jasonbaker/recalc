(ns test_parse
  (:use clojure.test
        recalc.parse))

(deftest num_test
  (is (= (parse int-lit "12") 12))
  (is (= (parse float-lit "12.2") 12.2))
  (is (= (parse number "12") 12))
  (is (= (parse number "12.2") 12.2)))
  