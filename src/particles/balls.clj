(ns particles.balls
  (:require
    [quil.core :as q]
    [particles.emitter :as emitter]
    [particles.util :refer :all]))

;; gravity, bounciness

(def max-particles 25)
(def rate 1)
(def bounciness 0.5)
(def rand-speed #(+ 40 (rand 10)))
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

(defn at-bottom? [size y]
  (> y (- (q/height) (/ size 2))))

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
  [{:keys [size pos acceleration velocity life start-life] :as p}]
  (let [[x y] pos
        [ax ay] acceleration
        [vx vy] velocity
        sign (if (at-bottom? size y) -1 1)]
    (-> p
        (update :velocity (partial update-vel sign acceleration))
        (update :pos (partial update-pos size sign velocity))
        (update :life #(- % (+ 0.10 (rand 0.2)))))))

(defn draw-pipe []
  (q/fill 100)
  (q/stroke-weight 3)
  (q/stroke 0 0 0)
  (q/rect (- (/ (q/width) 2) 20) (- (q/height) 64) 40 64))

;; -----------------------------------------------------------------------------

(defn setup []
  (q/frame-rate 60)
  [(particle)])

(def step
  (partial emitter/step emit-particle age-particle max-particles rate))

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

