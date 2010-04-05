(ns test_parse
  (:use clojure.test
        recalc.parse))

(deftest num_test
  (is (=  (parse number "12") 12)))
  