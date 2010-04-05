(ns test_parse
  (:use clojure.test
        recalc.parse))

(deftest num_test
  (is (=  (parse int-lit "12") 12)))
  