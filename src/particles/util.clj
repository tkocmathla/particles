(ns particles.util)

(def radians #(/ (* % Math/PI) 180))

(def map+ (partial map +))
(def map* (partial map *))
