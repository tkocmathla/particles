(ns particles.balls
  (:require
    [quil.core :as q]
    [particles.emitter :as emitter]
    [particles.util :refer :all]))

(def max-particles 10)
(def bounciness 0.7)
(def rand-speed #(+ 20 (rand 10)))
(def rand-angle #(+ 87 (rand 6)))
(def rand-size #(+ 16 (rand-int 16)))
(def rand-color #(rand-nth [[255 0 0] [0 255 0] [0 0 255]]))

(defn particle []
  (let [angle (rand-angle), speed (rand-speed)]
    {:pos [(/ (q/width) 2) (q/height)]
     :acceleration [0 0.25]
     :velocity [(* speed (Math/cos (radians angle)))
                (* speed (Math/sin (radians angle)) -1)] 
     :size (rand-size)
     :color (rand-color)
     :start-life 100
     :life 100}))

(defn old? [p] (-> p :life pos? not))

(defn emit-particle
  ([] (particle))
  ([p] (if (old? p) (particle) p)))

(defn update-vel [sign [ax ay] [vx vy]]
  [(+ vx ax) (if (neg? sign)
               (* vy bounciness sign)
               (+ vy ay))])

(defn update-pos [size sign [vx vy] [x y :as pos]]
  (if (neg? sign)
    [(+ x vx) (- (q/height) (/ size 2))]
    [(+ x vx) (+ y vy)]))

(defn age-particle
  [{:keys [size pos acceleration velocity life start-life] :as m}]
  (let [[x y] pos
        [ax ay] acceleration
        [vx vy] velocity
        sign (if (> y (- (q/height) (/ size 2))) -1 1)]
    (-> m
        (update :velocity (partial update-vel sign acceleration))
        (update :pos (partial update-pos size sign velocity))
        (update :life #(- % 0.2)))))

(defn draw-pipe []
  (q/fill 100)
  (q/stroke-weight 3)
  (q/stroke 0 0 0)
  (q/rect (- (/ (q/width) 2) 15) (- (q/height) 64) 30 64))

;; -----------------------------------------------------------------------------

(defn setup []
  (q/frame-rate 60)
  (repeatedly max-particles particle))

(def step
  (partial emitter/step emit-particle age-particle max-particles 1))

(defn draw [particles]
  (q/background 16 16 16)
  (doseq [{:keys [pos size color life start-life]} (remove old? particles)] 
    (q/push-matrix)
    (q/no-stroke)
    (apply q/fill color)
    (apply q/translate pos)
    (q/ellipse 0 0 size size)
    (q/pop-matrix))
  (draw-pipe))

