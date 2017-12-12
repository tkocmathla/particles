(ns particles.sketches.fountain
  (:require
    [quil.core :as q]
    [quil.middleware :as m]
    [particles.fountain :as fountain]))

(q/defsketch fountain
  :title "Fountain"
  :size [750 750]
  :setup fountain/setup
  :update fountain/step
  :draw fountain/draw
  :renderer :p2d
  :features [:keep-on-top :no-safe-fns]
  :middleware [m/fun-mode])

