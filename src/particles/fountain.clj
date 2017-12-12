(ns particles.fountain
  (:require
    [quil.core :as q]
    [particles.util :refer :all]))

(def rand-speed #(+ 15 (rand 3)))
(def rand-angle #(+ 60 (rand 8)))

(defn particle []
  (let [speed (rand-speed), angle (rand-angle)]
    {:pos [100 (q/height)] 
     :acceleration [0 0.25]
     :velocity [(* speed (Math/cos (radians angle)))
                (* speed (Math/sin (radians angle)) -1)] 
     :size 32}))

(defn off-screen? [{:keys [pos]}]
  (let [[x y] pos]
    (or (neg? x) (> x (q/width))
        (neg? y) (> y (q/height)))))

(defn emit-particle [p]
  (if (off-screen? p) (particle) p))

(defn move-particle
  [{:keys [velocity acceleration] :as m}]
  (-> m
      (update :velocity map+ acceleration)
      (update :pos map+ velocity)))

;; -----------------------------------------------------------------------------

(defonce image (atom nil))

(defn setup []
  (q/frame-rate 60)
  (q/blend-mode :add)
  (reset! image (q/load-image "particleTexture.png"))
  (q/resize @image 32 0)
  (repeatedly 1000 particle))

(defn step [particles]
  (map (comp emit-particle move-particle) particles))

(defn draw [particles]
  (q/background 16 16 16)
  (q/no-stroke)
  (doseq [{:keys [pos size]} (remove off-screen? particles)] 
    (q/push-matrix)
    (apply q/translate pos)
    (q/begin-shape)
    (q/tint 0 32 128 64)
    (q/texture @image)
    (q/vertex 0 0 0 0)
    (q/vertex size 0 size 0)
    (q/vertex size size size size)
    (q/vertex 0 size 0 size)
    (q/end-shape :close)
    (q/pop-matrix)))
