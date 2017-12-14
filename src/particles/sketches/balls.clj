(ns particles.sketches.balls
  (:require
    [quil.core :as q]
    [quil.middleware :as m]
    [particles.balls :as balls]))

(q/defsketch balls
  :title "Balls"
  :size [750 750]
  :setup balls/setup
  :update balls/step
  :draw balls/draw
  :renderer :opengl
  :features [:keep-on-top :no-safe-fns]
  :middleware [m/fun-mode])

