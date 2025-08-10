(ns particles.sketches.smoke
  (:require
    [quil.core :as q]
    [quil.middleware :as m]
    [particles.smoke :as smoke]))

(q/defsketch smoke
  :title "Smoke"
  :size [750 750]
  :setup smoke/setup
  :update smoke/step
  :draw smoke/draw
  :renderer :opengl
  :features [:keep-on-top :no-safe-fns]
  :middleware [m/fun-mode])
