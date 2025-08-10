(ns particles.core
  (:gen-class)
  (:require
    [particles.sketches.balls :as balls]
    [particles.sketches.fire :as fire]
    [particles.sketches.fireworks :as fireworks]
    [particles.sketches.fountain :as fountain]
    [particles.sketches.smoke :as smoke]))

(def sketches
  {"balls" balls/run
   "fire" fire/run
   "fireworks" fireworks/run
   "fountain" fountain/run
   "smoke" smoke/run})

(defn -main
  [& [sketch]]
  (if-let [runner (get sketches sketch)]
    (runner)
    (do
      (println "Run a sketch with:")
      (println "  lein run <sketch>")
      (println "Available sketches:")
      (doseq [name (keys sketches)]
        (println " -" name))
      (System/exit 1))))
