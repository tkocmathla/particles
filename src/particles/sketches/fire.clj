(ns particles.sketches.fire
  (:require
    [quil.core :as q]
    [quil.middleware :as m]
    [particles.fire :as fire]))

(q/defsketch fire
  :title "Fire"
  :size [750 750]
  :setup fire/setup
  :update fire/step
  :draw fire/draw
  :renderer :opengl
  :features [:keep-on-top :no-safe-fns]
  :middleware [m/fun-mode])
