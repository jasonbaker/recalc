(ns recalc
  (:gen-class)
  (:use recalc.parse))

(defn -main [& args]
  (println (parse "01")))