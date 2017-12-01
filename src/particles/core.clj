(ns particles.core
  (:require 
    [quil.core :as q]
    [quil.middleware :as m]))

(def radians #(/ (* % Math/PI) 180))

(def map+ (partial map +))
(def map* (partial map *))

(def rand-angle #(- 102.5 (rand-int 25)))
(def rand-speed #(max 7 (rand 12)))
(def rand-gray #(let [c (+ 32 (rand-int 64))] [c c c (rand-int 256)]))
(def rand-size #(let [x (rand-int 10)] [x x]))

(defn particle [angle speed]
  (let [max-life 10]
    {:pos [(+ 350 (rand (- (q/width) 700))) (q/height)]
     :velocity [(* speed (Math/cos (radians angle)))
                (* speed (Math/sin (radians angle)) -1)] 
     :color (rand-gray)
     :size (rand-size)
     :start-life max-life
     :life max-life}))

(def rand-particle #(particle (rand-angle) (rand-speed)))

(defn emit-particle [particles]
  (into particles (repeatedly (rand-int 25) rand-particle)))

(defn age-particle
  [{:keys [velocity life start-life] :as m}]
  (-> m
      (update :pos map+ velocity)
      (update :size map* (repeat (/ life start-life)))
      (update :life #(- % 0.01))))

(defn old? [p] (-> p :life pos? not))

(defn step [particles]
  (->> particles
       emit-particle
       (map age-particle)
       (remove old?)))

;; -----------------------------------------------------------------------------

(defn setup []
  (q/frame-rate 30)
  (repeatedly 10 rand-particle))

(defn draw [particles]
  (q/background 54 69 79) ; charcoal gray
  (q/no-stroke)
  (doseq [{:keys [pos color size life]} particles] 
    (q/push-matrix)
    (apply q/translate pos)
    (apply q/fill color)
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
