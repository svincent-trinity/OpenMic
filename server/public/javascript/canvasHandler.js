
    let takenSpaces = []

    //var recButton = document.getElementById("startRec");
    var myCanvas = document.getElementById("sequencerCanvas");
    var ctx = myCanvas.getContext("2d");   
     let s = 28
     let pL = s
     let pT = s
     let pR = s
     let pB = s
     
     let rectanglesX = []
     let rectanglesY = []
   ctx.strokeStyle = 'black'
   ctx.beginPath()
   for (var x = pL; x <= 800 - pR; x += s) {

      ctx.moveTo(x, pT)
      ctx.lineTo(x, 400 - pB)
   }
   for (var y = pT; y <= 400 - pB; y += s/2) {
    var btn = document.createElement("BUTTON");

      ctx.moveTo(pL, y)
      ctx.lineTo(800 - pR, y)
   }

    for (var x = pL; x <= 800 - pR; x += s) {
      for (var y = pT; y <= 400 - pB; y += s/2) {
        //ctx.fillStyle = "#000000"
        //ctx.fillRect(x, y, x+2.8, y+1.4)
        rectanglesX.push(x)
        rectanglesY.push(y)

     }
    }
   ctx.stroke()
    isPressingShift = false
    function drawGridElement(xPos, yPos, isAdding) {
       if(isAdding) {
        ctx.fillStyle = "#FF0000"

        ctx.fillRect(rectanglesX[xPos]+1, rectanglesY[yPos]+1, 26, 12)

      } else {
        let correctIndex = 0;
        for(let i=0; i < indexesOfReds.length; i++) {
          if(indexesOfReds[i] == [xPos, yPos]) correctIndex=i
        }
        //indexesOfReds.splice(correctIndex, 1)


        ctx.fillStyle = "#FFFFFF"
        ctx.fillRect(rectanglesX[xPos]+1, rectanglesY[yPos]+1, 26, 12)
        console.log(rectanglesX[0])
        console.log(myCanvas.offsetTop)
        console.log(document.documentElement.scrollTop)
      }

    }
    let indexesOfReds = []
       myCanvas.addEventListener('mousedown', (e) => {
         console.log(indexesOfReds)
         let pos = {
          x: e.clientX-myCanvas.offsetLeft,
          y: e.clientY-myCanvas.offsetLeft+document.documentElement.scrollTop

          };
       
          let realX=0;
          let ix = 0;
          let realY=0;
          let iy = 0;


        if(event.shiftKey) {
        drawGridElement(ix, iy, false)
        indexesOfReds.pop()

        } else {
                  for(let i=0; i < rectanglesX.length; i++) {
            if(rectanglesX[i] < pos.x) {
                realX = rectanglesX[i]

                ix = i;
            }
        }
        for(let i=0; i < rectanglesY.length; i++) {
            if(rectanglesY[i] < pos.y) {
                realY = i

                iy = i
            }
        }
        if(!indexesOfReds.includes([ix,iy])){
        indexesOfReds.push([ix,iy])
      }
              drawGridElement(ix, iy, true)

        }

    });
  ctx.beginPath()
  ctx.lineWidth = 5
  ctx.moveTo(28,26)
  ctx.lineTo(28,400-28)
  ctx.strokeStyle = "blue"
  ctx.stroke()
  let lastPos = 28
let toggled = true
myCanvas.addEventListener('keydown', (e) => {
    if(e.keyCode == 32){
        //your code
        console.log("space pressed")
        toggled=!toggled
        lastPos = 28
    }
});

setInterval(spacebarToggle, 50);

function drawBoard() {
  ctx.lineWidth = 1
   ctx.strokeStyle = 'black'
   ctx.beginPath()
   for (var x = pL; x <= 800 - pR; x += s) {

      ctx.moveTo(x, pT)
      ctx.lineTo(x, 400 - pB)
   }
   for (var y = pT; y <= 400 - pB; y += s/2) {
    var btn = document.createElement("BUTTON");

      ctx.moveTo(pL, y)
      ctx.lineTo(800 - pR, y)
   }

    for (var x = pL; x <= 800 - pR; x += s) {
      for (var y = pT; y <= 400 - pB; y += s/2) {
        ctx.fillStyle = "#FFFFFF"
        ctx.fillRect(x, y, x+2.8, y+1.4)
        rectanglesX.push(x)
        rectanglesY.push(y)

     }
    }
   ctx.stroke()
}
function spacebarToggle() {
  if(lastPos > 800-28) lastPos=28
   if(toggled) {
    drawBoard()
    for(let i = 0; i < indexesOfReds.length; i++) {
      drawGridElement(indexesOfReds[i][0], indexesOfReds[i][1], true)
      if(rectanglesX[indexesOfReds[i][0]]-lastPos < 2 && rectanglesX[indexesOfReds[i][0]]-lastPos>-1) {
        console.log("At location" + rectanglesY[indexesOfReds[i][1]])
        let indexLocation = 350
        let midiStart = 60
        while (indexLocation >= 28) {
        if(rectanglesY[indexesOfReds[i][1]] == indexLocation) {
        getMIDIMessageArray([144, midiStart, 64])
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