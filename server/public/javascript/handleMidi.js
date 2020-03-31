console.log("js is here");

let noteInput = document.getElementById("notePlayed");

let kick = document.getElementById("kick");
let snare = document.getElementById("snare");
let hihat = document.getElementById("hihat");
let ride = document.getElementById("ride");
let stick = document.getElementById("stick-hit");
let crash = document.getElementById("crash");

let C4 = document.getElementById("C4");
let Db4 = document.getElementById("Db4");
let D4 = document.getElementById("D4");
let Eb4 = document.getElementById("Eb4");
let E4 = document.getElementById("E4");
let F4 = document.getElementById("F4");
let Gb4 = document.getElementById("Gb4");
let G4 = document.getElementById("G4");
let Ab4 = document.getElementById("Ab4");
let A4 = document.getElementById("A4");
let Bb4 = document.getElementById("Bb4");
let B4 = document.getElementById("B4");
let C5 = document.getElementById("C5");
let Db5 = document.getElementById("Db5");
let D5 = document.getElementById("D5");
let Eb5 = document.getElementById("Eb5");
let E5 = document.getElementById("E5");
let F5 = document.getElementById("F5");
let Gb5 = document.getElementById("F#5");
let G5 = document.getElementById("G5");
let Ab5 = document.getElementById("Ab5");
let A5 = document.getElementById("A5");
let Bb5 = document.getElementById("Bb5");
let B5 = document.getElementById("B5");
let C6 = document.getElementById("C6");


noteInput.value = "";
navigator.requestMIDIAccess()
    .then(onMIDISuccess, onMIDIFailure);

function stopAudio(audio) {
	audio.pause();
	audio.currentTime = 0;
	/*var sound = audio;
	var fadePoint = sound.duration - 2; 

    var fadeAudio = setInterval(function () {

        // Only fade if past the fade out point or not at zero already
        if ((sound.currentTime >= fadePoint) && (sound.volume <= 0.0)) {
            sound.volume -= 0.1;
        }
        // When volume at zero stop all the intervalling
        if (sound.volume <= 0.0) {
            clearInterval(fadeAudio);
        }
    }, 200);*/
}

function onMIDISuccess(midiAccess) {
    console.log(midiAccess);

    /*var inputs = midiAccess.inputs;
    var outputs = midiAccess.outputs;*/
        for (var input of midiAccess.inputs.values()) {
        input.onmidimessage = getMIDIMessage;
    }
}

function onMIDIFailure() {
    console.log('Could not access your MIDI devices.');
}

function getMIDIMessage(midiMessage) {
    console.log(midiMessage.data);
    if(midiMessage.data[0] == 144) {
    	console.log("You played note " + midiMessage.data[1]);
    	noteInput.value = midiMessage.data[1];
    	playNote(noteInput.value, true);
    } /*else if (midiMessage.data[0] == 128) {
    	playNote(noteInput.value, false)
    }*/
}

function playNote(noteIn, onOrOff) {
	if(noteIn == 48) {
        stopAudio(kick);
        if (onOrOff) kick.play();
	} else if (noteIn == 49) {
		stopAudio(stick);
        if (onOrOff) stick.play();
	} else if (noteIn == 50) {
		stopAudio(snare);
        if (onOrOff) snare.play();
	} else if (noteIn == 54) {
		stopAudio(hihat);
        if (onOrOff) hihat.play();
	} else if (noteIn == 56) {
		stopAudio(ride);
        if (onOrOff) ride.play();
	} else if (noteIn == 58) {
		stopAudio(crash);
        if (onOrOff) crash.play();
	} else if (noteIn == 60) {
		stopAudio(C4);
		if (onOrOff) C4.play();
	} else if (noteIn == 61) {
		stopAudio(Db4);
		if (onOrOff) Db4.play();
	}else if (noteIn == 62) {
		stopAudio(D4);
		if (onOrOff) D4.play();
	}else if (noteIn == 63) {
		stopAudio(Eb4);
		if (onOrOff) Eb4.play();
	}else if (noteIn == 64) {
		stopAudio(E4);
		if (onOrOff) E4.play();
	}else if (noteIn == 65) {
		stopAudio(F4);
		if (onOrOff) F4.play();
	}else if (noteIn == 66) {
		stopAudio(Gb4);
		if (onOrOff) Gb4.play();
	}else if (noteIn == 67) {
		stopAudio(G4);
		if (onOrOff) G4.play();
	}else if (noteIn == 68) {
		stopAudio(Ab4);
		if (onOrOff) Ab4.play();
	}else if (noteIn == 69) {
		stopAudio(A4);
		if (onOrOff) A4.play();
	}else if (noteIn == 70) {
		stopAudio(Bb4);
		if (onOrOff) Bb4.play();
	}else if (noteIn == 71) {
		stopAudio(B4);
		if (onOrOff) B4.play();
	} else if (noteIn == 72) {
		stopAudio(C5);
		if (onOrOff) C5.play();
	} else if (noteIn == 73) {
		stopAudio(Db5);
		if (onOrOff) Db5.play();
	}else if (noteIn == 74) {
		stopAudio(D5);
		if (onOrOff) D5.play();
	}else if (noteIn == 75) {
		stopAudio(Eb5);
		if (onOrOff) Eb5.play();
	}else if (noteIn == 76) {
		stopAudio(E5);
		if (onOrOff) E5.play();
	}else if (noteIn == 77) {
		stopAudio(F5);
		if (onOrOff) F5.play();
	}else if (noteIn == 78) {
		stopAudio(Gb5);
		if (onOrOff) Gb5.play();
	}else if (noteIn == 79) {
		stopAudio(G5);
		if (onOrOff) G5.play();
	}else if (noteIn == 80) {
		stopAudio(Ab5);
		if (onOrOff) Ab5.play();
	}else if (noteIn == 81) {
		stopAudio(A5);
		if (onOrOff) A5.play();
	}else if (noteIn == 82) {
		stopAudio(Bb5);
		if (onOrOff) Bb5.play();
	}else if (noteIn == 83) {
		stopAudio(B5);
		if (onOrOff) B5.play();
	}else if (noteIn == 84) {
		stopAudio(C6);
		if (onOrOff) C6.play();
	}
}