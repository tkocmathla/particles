(ns particles.core
  (:require 
    [quil.core :as q]
    [quil.middleware :as m]))

(def radians #(/ (* % Math/PI) 180))
(def max-life 1000)

(defn particle [{:keys [x y angle speed]}]
  {:x x, :y y 
   :velocity [(* speed (Math/cos (radians angle)))
              (* speed (Math/sin (radians angle)) -1)] 
   :color [255 0 0]
   :size [20 20]
   :life max-life})

(defn rand-particle []
  (particle
    {:x (/ (q/width) 2)
     :y (/ (q/height) 2)
     :angle (rand-int 360)
     :speed (rand 10)}))

(defn setup []
  (q/frame-rate 60)
  (repeatedly 10 rand-particle))

(defn step [particles]
  (concat
    (repeatedly 5 rand-particle)
    (keep
      (fn [{x :x, y :y, [vx vy] :velocity, life :life, :as m}]
        (when (pos? life)
          (-> m
              (update :x + vx)
              (update :y + vy)
              (update :size (partial map *) (repeat (/ life max-life)))
              (update :life dec))))
      particles)))

(defn draw [particles]
  (q/background 54 69 79) ; charcoal gray
  (q/no-stroke)
  (doseq [{:keys [x y color size life]} particles] 
    (q/push-matrix)
    (q/translate x y)
    (let [[r g b] color] (q/fill r g b life))
    (let [[w h] size] (q/ellipse 0 0 w h))
    (q/pop-matrix)))

(q/defsketch particles
  :title "Particle system"
  :size [750 750]
  :setup setup
  :update step
  :draw draw
  :features [:keep-on-top]
  :middleware [m/fun-mode])
