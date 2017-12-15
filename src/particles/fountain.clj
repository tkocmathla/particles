(ns particles.fountain
  (:require
    [quil.core :as q]
    [particles.emitter :as emitter]
    [particles.util :refer :all]))

;; texture, blending, opacity

(def max-particles 1000)
(def rate 5)
(def rand-speed #(+ 15 (rand 3)))
(def rand-angle #(+ 60 (rand 8)))
(def rand-alpha #(rand-int 175))

(defn particle []
  (let [speed (rand-speed), angle (rand-angle)]
    {:pos [100 (q/height)] 
     :color [0 32 128]
     :alpha (rand-alpha) 
     :acceleration [0 0.25]
     :velocity [(* speed (Math/cos (radians angle)))
                (* speed (Math/sin (radians angle)) -1)] 
     :size 32}))

(defn off-screen? [{:keys [pos]}]
  (let [[x y] pos]
    (or (neg? x) (> x (q/width))
        (neg? y) (> y (q/height)))))

(defn emit-particle
  ([] (particle))
  ([p] (if (off-screen? p) (particle) p)))

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
  [(particle)])

(def step
  (partial emitter/step emit-particle move-particle max-particles rate))

(defn draw [particles]
  (q/background 16 16 16)
  (q/no-stroke)
  (doseq [{:keys [pos size color alpha]} (remove off-screen? particles)] 
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
