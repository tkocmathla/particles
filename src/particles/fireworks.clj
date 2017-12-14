(ns particles.fireworks
  (:require
    [quil.core :as q]
    [particles.emitter :as emitter]
    [particles.util :refer :all]))

(def max-particles 1000)
(def rand-speed #(+ 5 (rand 30)))
(def rand-angle #(rand 360))
(def rand-alpha #(+ 127 (rand 127)))
(def rand-pos #(vector (rand (q/width)) (rand (q/height))))

(defn particle [pos]
  (let [speed (rand-speed), angle (rand-angle)]
    {:pos pos
     :color [0 32 128]
     :alpha (rand-alpha) 
     :acceleration [0 0.05]
     :velocity [(* speed (Math/cos (radians angle)))
                (* speed (Math/sin (radians angle)) -1)] 
     :size 8
     :life 255}))

(defn old? [p] (-> p :life pos? not))

(defn emit-particle
  ([] (particle (rand-pos)))
  ([pos] (particle pos))
  ([pos p] (if (old? p) (particle pos) p)))

(defn move-particle
  [{:keys [velocity acceleration] :as m}]
  (-> m
      (update :velocity map+ acceleration)
      (update :pos map+ velocity)
      (update :life - 1)))

;; -----------------------------------------------------------------------------

(defonce image (atom nil))

(defn setup []
  (q/frame-rate 60)
  (q/blend-mode :add)
  (reset! image (q/load-image "particleTexture.png"))
  (q/resize @image 8 0)
  [])

(defn step [particles]
  (emitter/step (partial emit-particle (rand-pos)) move-particle max-particles max-particles particles))

(defn draw [particles]
  (q/background 16 16 16)
  (q/no-stroke)
  (doseq [{:keys [pos size color alpha]} particles] 
    (q/push-matrix)
    (apply q/translate pos)
    (q/begin-shape)
    (let [[r g b] color] (q/tint r g b alpha))
    (q/texture @image)
    (q/vertex 0 0 0 0)
    (q/vertex size 0 size 0)
    (q/vertex size size size size)
    (q/vertex 0 size 0 size)
    (q/end-shape :close)
    (q/pop-matrix)))
