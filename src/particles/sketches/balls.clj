(ns particles.sketches.balls
  (:require
    [quil.core :as q]
    [quil.middleware :as m]
    [particles.balls :as balls]))

(defn run []
  (q/defsketch balls
    :title "Balls"
    :size [750 750]
    :setup balls/setup
    :update balls/step
    :draw balls/draw
    :renderer :opengl
    :features [:no-safe-fns :keep-on-top :exit-on-close]
    :middleware [m/fun-mode]))
