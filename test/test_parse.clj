(ns test_parse
  (:use clojure.test
        recalc.parse))

(deftest num_test
  (is (= (:content (parse int-lit "12")) 12))
  (is (= (:content (parse float-lit "12.2")) 12.2))
  (is (= (:content (parse number "12")) 12))
  (is (= (:content (parse number "12.2")) 12.2))
  (is (= (:kind (parse number "12")) :scalar))
  (is (= (:kind (parse number "12.2")) :scalar)))
  
(deftest letter_test
  (is (= (parse letter "a") \a))
  (is (= (:content (parse identifier "abc123")) "abc123"))
  (is (= (:kind (parse identifier "abc123")) :variable)))

(deftest projection_test
  (is (= (:content (parse projection "foo")) ["foo"]))
  (is (= (:content (parse projection "foo bar baz")) ["foo" "bar" "baz"])))

(deftest expression_test
  (is (= (:content (parse expression "1")) 1))
  (is (= (:content (parse expression "asdf ")) "asdf")))