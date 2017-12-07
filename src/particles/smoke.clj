(ns particles.smoke
  (:require
    [quil.core :as q]
    [particles.util :refer :all]))

(def rand-pos #(vector (+ 300 (rand (- (q/width) 600))) (q/height)))
(def rand-speed #(max 0.1 (rand 3)))
(def rand-life #(inc (rand-int 8)))
(def rand-gray #(let [c (+ 32 (rand-int 64))] [c c c (rand-int 256)]))

(defn old? [p] (-> p :life pos? not))

(defn particle []
  (let [life (rand-life), speed (rand-speed)]
    {:pos (rand-pos)
     :velocity [(* speed (Math/cos (radians 90)))
                (* speed (Math/sin (radians 90)) -1)] 
     :color (rand-gray)
     :size [16 16]
     :start-life life
     :life life}))

(defn emit-particle [p]
  (cond-> p (old? p) (assoc :pos (rand-pos) :life (rand-life))))

(defn age-particle
  [{:keys [velocity life start-life] :as m}]
  (-> m
      (update :pos map+ velocity)
      (update :life #(- % (rand 0.07)))))

;; -----------------------------------------------------------------------------

(defonce image (atom nil))

(defn setup []
  (q/frame-rate 60)
  (q/blend-mode :add)
  (reset! image (q/load-image "particleTexture16.png"))
  (repeatedly 1000 particle))

(defn step [particles]
  (map (comp emit-particle age-particle) particles))

(defn draw [particles]
  (q/background 0 0 0)
  (q/no-stroke)
  (doseq [{:keys [pos color size life start-life]} (remove old? particles)] 
    (q/push-matrix)
    (apply q/translate pos)
    (q/begin-shape)
    (q/texture @image)
    (let [[r g b] color]
      (q/tint r g b (* 255 (/ life start-life))))
    (let [[w h] size]
      (q/vertex 0 0 0 0)
      (q/vertex w 0 w 0)
      (q/vertex w h w h)
      (q/vertex 0 h 0 h))
    (q/end-shape :close)
    (q/pop-matrix)))
