package edu.trinity.videoquizreact

import edu.trinity.videoquizreact.shared.SharedMessages
import org.scalajs.dom

import slinky.core._
import slinky.web.ReactDOM
import slinky.web.html._

import org.scalajs.dom
import org.scalajs.dom.document
import scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel
import org.scalajs.dom.html

import org.scalajs.dom.raw.HTMLImageElement
import scala.collection.mutable.ArrayBuffer


object ScalaJSExample {
	var s = 28
	var pL = s
    var pT = s
    var pR = s
    var pB = s

    var rectanglesX = new ArrayBuffer[Int]()
    var rectanglesY = new ArrayBuffer[Int]()

    var indexesOfReds = new ArrayBuffer[Array[Int]]()

    var isPressingShift = false;

    val myCanvas = document.getElementById("sequencerCanvas").asInstanceOf[html.Canvas]
	val ctx = myCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

	var lastPos = 28
	var toggled=true

    def main(args: Array[String]): Unit = {
    // dom.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks

	    println("TestPrintJS")
	    dom.window.addEventListener("mousedown", {(e: dom.KeyboardEvent) =>
        	println("Mouse Click")
  		})
        

    }

    def drawGridElement(xPos:Int, yPos:Int, isAdding:Boolean) {
       if(isAdding) {
	        ctx.fillStyle = "#FF0000"

	        ctx.fillRect(rectanglesX(xPos)+1, rectanglesY(yPos)+1, 26, 12)

        } else {
	        var correctIndex = 0;
	        for(i <- 0 to indexesOfReds.size) {
	            if(indexesOfReds(i) == Array(xPos, yPos)) correctIndex=i
	        }
	        //indexesOfReds.splice(correctIndex, 1)


	        ctx.fillStyle = "#FFFFFF"
	        ctx.fillRect(rectanglesX(xPos)+1, rectanglesY(yPos)+1, 26, 12)

        }

    }

    def spacebarToggle() {
		if(lastPos > 800-28) lastPos=28
	    if(toggled) {
		    drawBoard()
		    for(i <- 0 to indexesOfReds.size) {
		        drawGridElement(indexesOfReds(i)(0), indexesOfReds(i)(1), true)
		        if(rectanglesX(indexesOfReds(i)(0))-lastPos < 2 && rectanglesX(indexesOfReds(i)(0))-lastPos > -1) {
				    println("At location" + rectanglesY(indexesOfReds(i)(1)))
				    var indexLocation = 350
			        var midiStart = 60
			        while (indexLocation >= 28) {
				        if(rectanglesY(indexesOfReds(i)(1)) == indexLocation) {
				        //getMIDIMessageArray((144, midiStart, 64))

				        }
				        indexLocation -= 14
				        midiStart += 1;
				    }
			        //playNote(48, true)
			    }
			}
		    lastPos += 2
		    ctx.beginPath()
		    ctx.lineWidth = 5
		    ctx.moveTo(lastPos,26)
		    ctx.lineTo(lastPos,400-28)
		    ctx.strokeStyle = "blue"
		    ctx.stroke()
	    }
    }

    def drawBoard() {
        ctx.lineWidth = 1
	    ctx.strokeStyle = "black"
	    ctx.beginPath()
	    var x = pL
	    while(x <= 800-pR) {
	    	ctx.moveTo(x, pT)
	        ctx.lineTo(x, 400 - pB)
	    	x += s
	    }
	    /*for (var x = pL; x <= 800 - pR; x += s) {

	        ctx.moveTo(x, pT)
	        ctx.lineTo(x, 400 - pB)
	    }*/
	    var y = pT
	    while(y <= 400-pB) {
	    	ctx.moveTo(pL, y)
	        ctx.lineTo(800 - pR, y)
	    	y += s/2
	    }
	    /*for (var y = pT; y <= 400 - pB; y += s/2) {
	        ctx.moveTo(pL, y)
	        ctx.lineTo(800 - pR, y)
	    }*/
        var z = pL
        while(z <= 800-pR) {
        	var a1 = pT
        	while(a1 <= 400-pB) {
        		ctx.fillStyle = "#FFFFFF"
		        ctx.fillRect(x, y, x+2.8, y+1.4)
		        rectanglesX.append(x)
		        rectanglesY.append(y)
        		a1 += s/2
        	}
        	z += s
        }
	    /*for (var x = pL; x <= 800 - pR; x += s) {
	        for (var y = pT; y <= 400 - pB; y += s/2) {
		        ctx.fillStyle = "#FFFFFF"
		        ctx.fillRect(x, y, x+2.8, y+1.4)
		        rectanglesX.push(x)
		        rectanglesY.push(y)
	        }
        }*/
        ctx.stroke()
    }
}

