const notesFromDb = document.getElementById("notesFromDb").value;
const updateMidiDb = document.getElementById("updateMidiDb").value;


  function getNotes(id) {
    console.log("project " + id + " is playing")
    //Get song by id here
    fetch(notesFromDb, {
        method: 'POST',
        headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
        body: JSON.stringify(id.toString())
    }).then(res => res.json()).then(data => {
      console.log(data)
      if(data === "None") {
         console.log("no data")
      } else {
             var tmp = data.split(",")
      var parsed = []
      for(let i=0; i<tmp.length; i++) {
        if(i%2!=0) {
          parsed.push([tmp[i-1], tmp[i]])
        }
      }
      console.log(parsed)
      indexesOfReds = parsed
      }
        /*if(data) {
          if(this.hasSong) {
            document.getElementById("tmpMaker").style.display="none";
          }
          var song = document.createElement("AUDIO");
          song.setAttribute("src", document.getElementById("filePath").value)
          song.setAttribute("controls", "controls");
          song.setAttribute("id", "tmpMaker");
          document.body.appendChild(song);
          this.setState( { hasSong: true } );
          console.log("yay")
        } else {
                     //TODO
          console.log("Error: Could not play song")
          //this.setState({ taskMessage: "Failed to delete" })
        } */
    });

    //Play song:

  }

  function updateMidi(id, midiNotes) {
    let putTogether = id.toString()

    for(let i=0; i<midiNotes.length; i++) {
      putTogether += "####"
      putTogether += midiNotes[i][0].toString()
      putTogether += "####"
      putTogether += midiNotes[i][1].toString()
    }
    console.log(putTogether)
      fetch(updateMidiDb, {
          method: 'POST',
          headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
          body: JSON.stringify(putTogether)
      }).then(res => res.json()).then(data => {
        console.log(data)
        if(data) {
           console.log("Good")
        } else {
          console.log("Bad")
          //indexesOfReds = parsedData
        }

      });

  
  }
    let takenSpaces = []

    const socketRoute = document.getElementById("ws-route").value;
    console.log(socketRoute)
    let socket = new WebSocket(socketRoute.replace("http", "ws"));




    //var recButton = document.getElementById("startRec");
    var myCanvas = document.getElementById("sequencerCanvas");
    var ctx = myCanvas.getContext("2d"); 


    socket.onopen = () => { 
      //ctx.beginPath();
      //ctx.arc(0, 0, 10, 0, 2*Math.PI);
      //ctx.strokeStyle = myColor;
      //ctx.stroke();
      //socket.send("New user connected.");
      //let retString = (0 + " " + 0 + " " + myColor);
      //  socket.send(retString);
      //updateMidi(projectId, indexesOfReds)
    }
    socket.onmessage = (event) => {
      //outputArea.value += '\n' + event.data;
      console.log("This is from the websocket!")
      console.log(event.data.split(","))
      let split = event.data.split(",")
      console.log(event.data[0])
      indexesOfReds.push([split[0],split[1]])
      console.log(indexesOfReds)
      updateMidi(projectId, indexesOfReds)
      /*const split = event.data.split(",")
      let tmp = split[0]
      let tmpArr = []
      for(let i = 0; i < split.Length-1; i++) {
        tmpArr.push([split[i], split[i+1]])
      }
      indexesOfReds = tmpArr
      console.log(indexesOfReds)*/
    }
    socket.onclose = function(){
        setTimeout(setupWebSocket, 1000);
    };

    function setupWebSocket() {
      socket = new WebSocket(socketRoute.replace("http", "ws"));
    }

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
          y: e.clientY-myCanvas.offsetTop+document.documentElement.scrollTop

          };
       
          let realX=0;
          let ix = 0;
          let realY=0;
          let iy = 0;

        if(event.space) {
          console.log("space pressed")
          toggled=!toggled
          lastPos = 28
        }
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
        //indexesOfReds.push([ix,iy])
        socket.send([ix,iy])
        //socket.send(indexesOfReds)
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
window.addEventListener('keydown', (e) => {
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