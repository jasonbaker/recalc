(ns recalc.parse
  (:use name.choi.joshua.fnparse
        clojure.contrib.error-kit))

(defstruct node-s :kind :content)
