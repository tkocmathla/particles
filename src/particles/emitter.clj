(ns particles.emitter)

(defn step [emit-fn age-fn max-particles rate particles]
  (let [x (- max-particles (count particles))]
    (cond->> (map (comp emit-fn age-fn) particles)
      (pos? x) (into (repeatedly rate emit-fn))
      (neg? x) (drop (* x -1)))))

