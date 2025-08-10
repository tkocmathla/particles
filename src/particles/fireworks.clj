(ns particles.fireworks
  (:require
    [quil.core :as q]
    [particles.emitter :as emitter]
    [particles.util :refer :all]))

;; moving emitter 

(def max-particles 1000)
(def rate max-particles)
(def rand-speed #(+ 5 (rand 30)))
(def rand-angle #(rand 360))
(def rand-pos #(vector (rand (q/width)) (rand (q/height))))
(def rand-tint #(vector (rand-int 256) 255 (rand-int 256)))

(defn particle [pos]
  (let [speed (rand-speed), angle (rand-angle)]
    {:pos pos
     :acceleration [0 0.05]
     :velocity [(* speed (Math/cos (radians angle)))
                (* speed (Math/sin (radians angle)) -1)] 
     :color (rand-tint)
     :size 9
     :life 175}))

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
  [])

(defn step [particles]
  (emitter/step (partial emit-particle (rand-pos)) move-particle max-particles rate particles))

(defn draw [particles]
  (q/background 16 16 16)
  (q/no-stroke)
  (doseq [{:keys [pos size color]} particles]
    (q/push-matrix)
    (apply q/translate pos)
    (q/begin-shape)
    (let [[r g b] color] (q/tint r g b 255))
    (q/texture @image)
    (q/vertex 0 0 0 0)
    (q/vertex size 0 size 0)
    (q/vertex size size size size)
    (q/vertex 0 size 0 size)
    (q/end-shape :close)
    (q/pop-matrix)))
