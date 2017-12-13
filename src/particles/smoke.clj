(ns particles.smoke
  (:require
    [quil.core :as q]
    [particles.emitter :as emitter]
    [particles.util :refer :all]))

(def max-particles 1000)
(def rand-pos #(vector (+ 300 (rand (- (q/width) 600))) (q/height)))
(def rand-speed #(max 0.1 (rand 3)))
(def rand-life #(inc (rand-int 9)))

(defn old? [p] (-> p :life pos? not))

(defn particle []
  (let [start-life (rand-life)
        life (min start-life (rand-life))
        speed (rand-speed)]
    {:pos (rand-pos)
     :velocity [(* speed (Math/cos (radians 90)))
                (* speed (Math/sin (radians 90)) -1)] 
     :size 64
     :start-life start-life
     :life life
     :alpha 255}))

(defn emit-particle
  ([] (particle))
  ([p] (if (old? p) (particle) p)))

(defn age-particle
  [{:keys [velocity life start-life] :as m}]
  (-> m
      (update :velocity map* (repeat 0.996))
      (update :pos map+ velocity)
      (assoc :alpha (* 255 (/ life start-life)))
      (update :life #(- % (rand 0.05)))))

;; -----------------------------------------------------------------------------

(defonce image (atom nil))

(defn setup []
  (q/frame-rate 90)
  (q/blend-mode :add)
  (reset! image (q/load-image "particleTexture.png"))
  (q/resize @image 64 0)
  [(particle)])

(def step
  (partial emitter/step emit-particle age-particle max-particles 10))

(defn draw [particles]
  (q/background 16 16 16)
  (q/no-stroke)
  (doseq [{:keys [pos alpha size life start-life]} (remove old? particles)] 
    (q/push-matrix)
    (apply q/translate pos)
    (q/begin-shape)
    (q/texture @image)
    (q/tint 8 8 8 alpha)
    (q/vertex 0 0 0 0)
    (q/vertex size 0 size 0)
    (q/vertex size size size size)
    (q/vertex 0 size 0 size)
    (q/end-shape :close)
    (q/pop-matrix)))
