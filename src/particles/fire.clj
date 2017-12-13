(ns particles.fire
  (:require
    [quil.core :as q]
    [particles.emitter :as emitter]
    [particles.util :refer :all]))

(def max-particles 1000)
(def rand-pos #(vector (+ 300 (rand (- (q/width) 600))) (q/height)))
(def rand-speed #(max 1.5 (rand 7)))
(def rand-life #(inc (rand-int 30)))
(defn rand-angle [x]
  (let [sign (if (> x (/ (q/width) 2)) 1 -1)]
    (+ 90 (* sign 6))))

(defn particle []
  (let [start-life (rand-life)
        life (min start-life (rand-life))
        [x y] (rand-pos)
        angle (rand-angle x)
        speed (rand-speed)]
    {:pos [x y]
     :velocity [(* speed (Math/cos (radians angle)))
                (* speed (Math/sin (radians angle)) -1)] 
     :size 100
     :start-life start-life
     :life life
     :alpha 255}))

(defn old? [p] (-> p :life pos? not))

(defn emit-particle
  ([] (particle))
  ([p] (if (old? p) (particle) p)))

(defn age-particle
  [{:keys [velocity life start-life] :as m}]
  (-> m
      (update :velocity map* (repeat 0.995))
      (update :pos map+ velocity)
      (assoc :alpha (* 255 (/ life start-life)))
      (update :life #(- % 0.2))))

;; -----------------------------------------------------------------------------

(defonce image (atom nil))

(defn setup []
  (q/frame-rate 60)
  (q/blend-mode :add)
  (reset! image (q/load-image "particleTexture.png"))
  (q/resize @image 100 0)
  (repeatedly max-particles particle))

(def step
  (partial emitter/step emit-particle age-particle max-particles 100))

(defn draw [particles]
  (q/background 16 16 16)
  (q/no-stroke)
  (doseq [{:keys [pos alpha size life start-life]} (remove old? particles)] 
    (q/push-matrix)
    (apply q/translate pos)
    (q/begin-shape)
    (q/tint 16 16 100 alpha)
    (q/texture @image)
    (q/vertex 0 0 0 0)
    (q/vertex size 0 size 0)
    (q/vertex size size size size)
    (q/vertex 0 size 0 size)
    (q/end-shape :close)
    (q/pop-matrix)))
