(ns particles.sketches.fireworks
  (:require
    [quil.core :as q]
    [quil.middleware :as m]
    [particles.fireworks :as fireworks]))

(q/defsketch fireworks
  :title "Fireworks"
  :size [750 750]
  :setup fireworks/setup
  :update fireworks/step
  :draw fireworks/draw
  :renderer :opengl
  :features [:keep-on-top :no-safe-fns]
  :middleware [m/fun-mode])
