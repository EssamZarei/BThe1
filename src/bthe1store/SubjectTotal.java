/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bthe1store;

/**
 *
 * @author eaz99
 */
public interface SubjectTotal {
     void addObserver(ObserverTotal observer);
     void removeObserver(ObserverTotal observer);
     void notifyObservers();
}
