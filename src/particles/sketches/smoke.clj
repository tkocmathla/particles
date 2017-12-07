(ns particles.sketches.smoke
  (:require
    [quil.core :as q]
    [quil.middleware :as m]
    [particles.smoke :as smoke]))

(q/defsketch particles
  :title "Particle system"
  :size [750 750]
  :setup smoke/setup
  :update smoke/step
  :draw smoke/draw
  :renderer :p2d
  :features [:keep-on-top :no-safe-fns]
  :middleware [m/fun-mode])
