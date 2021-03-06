console.log("running version 5")

const ce = React.createElement
const csrfToken = document.getElementById("csrfToken").value;
const validateRoute = document.getElementById("validateRoute").value;
const tasksRoute = document.getElementById("tasksRoute").value;
const publicsRoute = document.getElementById("publicsRoute").value;
const addRoute = document.getElementById("addRoute").value;
const deleteRoute = document.getElementById("deleteRoute").value;
const createRoute = document.getElementById("createRoute").value;
const pubRoute = document.getElementById("pubRoute").value;
const logoutRoute = document.getElementById("logoutRoute").value;
const usersRoute = document.getElementById("usersRoute").value;
const getUserRoute = document.getElementById("getUserRoute").value;
const recordingsList = document.getElementById("recordingsList").value;
const playRecording = document.getElementById("playSong").value;
const instrumentsList = document.getElementById("getInstruments").value;
const loadInstrumentPath = document.getElementById("loadInstrumentAudio").value;
const img = document.getElementById("banner").value;
const imgLogo = document.getElementById("logo").value;
//const notesFromDb = document.getElementById("notesFromDb").value;
const loadingGif = document.getElementById("loadingGif").value;
const searchProjects = document.getElementById("searchProjects").value;

let sessionUsername = "";
let projectId = 0;


const canv = document.getElementById("sequencerCanvas");
canv.style.display = "none"
const uploadDiv = document.getElementById("file_upload");
uploadDiv.style.display = "none"

const uploadInstrumentDiv = document.getElementById("instrument_upload");
uploadInstrumentDiv.style.display = "none"

let loadingScreen = ce('img', { id: "loader", src: loadingGif }, null);


class OpenMicMainComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state = { loggedIn: false, pageOn: "home" };
  }



  render() {
    if(this.state.loggedIn) {
      if(this.state.pageOn == "home") {
        console.log("home")
        return ce(HomePageComponent, { goToInstruments: () => this.setState( {pageOn: "instruments"} ), goToRecordings: () => this.setState( {pageOn: "recordings"} ), goToProject: () => this.setState( {pageOn: "project"} ), goToPublic: () => this.setState( {pageOn: "public"} ), doLogout: () => this.setState( {loggedIn: false, pageOn: "home"})});
      } else if (this.state.pageOn == "public") {
        console.log("public")
        return ce(PublicPageComponent, { goToProject: () => this.setState( {pageOn: "project"} ), goToHome: () => this.setState( {pageOn: "home"} ), doLogout: () => this.setState( {loggedIn: false, pageOn: "home"})});

      } else if (this.state.pageOn == "project") {
        console.log("project")
        return ce(ProjectPageComponent, { goToHome: () => this.setState( {pageOn: "home"} ), doLogout: () => this.setState( {loggedIn: false, pageOn: "home"})});

      }else if (this.state.pageOn == "recordings") {
        console.log("recordings")
        return ce(RecordingsPageComponent, { goToHome: () => this.setState( {pageOn: "home"} ), doLogout: () => this.setState( {loggedIn: false, pageOn: "home"})});

      }else if (this.state.pageOn == "instruments") {
        console.log("instruments")
        return ce(InstrumentsPageComponent, { goToHome: () => this.setState( {pageOn: "home"} ), doLogout: () => this.setState( {loggedIn: false, pageOn: "home"})});

      }
    } else {
      return ce(LoginComponent, {doLogin: () => this.setState( {loggedIn: true, pageOn: "home"}) });
  }
  }
}

class LoginComponent extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
      loginName: "",
      loginPass: "",
      createName: "",
      createPass: "",
      loginMessage: "",
      createMessage: ""

    };
	}

	render() {

    return ce('div', {},
        ce('img', { src: img, className: "loginBanner" }, null),
        ce('br'),
        ce('h2', {className: "loginText"}, 'Login: '),
        ce('br'),
        ce('p', {className: "loginUserName"}, 'Username: '),
        ce('input', {type: "text", id: "loginName", value: this.state.loginName, onChange: e=> this.changeHandler(e), className: "loginName"}),
        ce('br'),
        ce('p', {className: "loginPassName"}, 'Password: '),
        ce('input', {type: "password", id: "loginPass", value: this.state.loginPass, onChange: e=> this.changeHandler(e), className: "loginPass"}),
        ce('br'),
        ce('button', {onClick: e => this.login(e), className: "loginBtn"}, 'Login'),
        ce('br'),
        ce('span', {id: "login-message"}, this.state.loginMessage),
        ce('br'),
        ce('h2', {className: "createUserText"}, 'Create User:'),
        ce('br'),
        ce('p', {className: "createUserName"}, 'Username: '),
        ce('input', {type: "text", className: "createName", id: "createName", value: this.state.createName, onChange: e=> this.changeHandler(e)}),
        ce('br'),
        ce('p', {className: "createUserPassName"}, 'Password: '),
        ce('input', {type: "password", className: "createPass", id: "createPass", value: this.state.createPass, onChange: e=> this.changeHandler(e)}),
        ce('br'),
        ce('button', {onClick: e => this.createUser(e), className: "createUsrBtn"}, 'Create User'),
        ce('span', {id: "create-message"}, this.state.createMessage)

	);
	}

  runUsername() {
    sessionUsername = this.state.loginName;
  }
	changeHandler(e) {
    this.setState({ [e.target['id']]: e.target.value });
	}

  login(e) {
  /*const username = this.state.loginName;
  const password = this.state.loginPass;
  this.runUsername();
  fetch(validateRoute, {
    method: 'POST',
    headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
    body: JSON.stringify({ username, password })
  }).then(res => res.json()).then(data => {
        if(data) {
          this.props.doLogin();

        } else {
             //TODO
            this.setState({ loginMessage: "Login Failed" });
        }
    });*/
    this.props.doLogin();

  }
  createUser(e) {
    const username = this.state.createName;
    const password = this.state.createPass;
    fetch(createRoute, {
        method: 'POST',
        headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
        body: JSON.stringify({ username, password })
    }).then(res => res.json()).then(data => {
        if(data) {
            this.props.doLogin()
        } else {
             //TODO
            this.setState({ createMessage: "User Creation Failed" });

            //document.getElementById("create-message").innerHTML = "User Creation Failed";

        }
    });
  }

}

class HomePageComponent extends React.Component {

  constructor(props) {
    super(props);
    this.state = { projects: [], newProject: "", isPublic: ["Private", "Public"], userSelected: "Private", projectName: "", searchBar: "" };
  }

  componentDidMount() {
    indexesOfReds = []
    this.loadTasks();
  }


  render() {
    return ce('div', {},
      null ,

      ce('div', {/*className: "homePage"*/},
        ce('img', {src: imgLogo, className: "homePageLogo"}, null),
        ce('h2', {className: "welcomeText"}, 'Welcome, ' + sessionUsername + "!"),
        ce('h2', {className: "projectsText"}, 'My Projects'),
        ce('br'),
        ce('table', {className: "table"},
          ce('thead', null, ce('tr', null, ce('th', null, "Project Name"), ce('th', null, "Privacy"))),
            ce('tbody', null, this.state.projects.map(task => ce('tr', { key: task.id, onClick: e => { projectId=task.id; getNotes(projectId); console.log(projectId); this.props.goToProject() } }, ce('td', null, task.text), ce('td', null, task.isPublic))
              ), ce('tr', null, ce('td', null,
                ce('input', {type: 'text', value: this.state.newProject, placeholder: 'Create a new project', onChange: e => this.handleChange(e) })

              ), ce('td', null,
          ce('select', {onChange: e => this.handleSelectChange(e)},
            this.state.isPublic.map((us, index) => ce('option', { key: index }, us))
          )

              ), ce('td', null,

          ce('button', {onClick: e => this.handleAddClick(e)}, 'Create Project!'),
          this.state.projectName
          )
              )
            ),

            ),

                ce('br'),
        //ce('input', { className: "welcomeText", placeholder: 'Search for projects', onChange: e => this.handleProjectSearch(e)}),
        //ce('br'),

        //ce('button', { className: "welcomeText", onClick: e => this.searchForProject()}, 'Search'),

        ce('br')


        ),
        ce('div', {className: "homePageNav"},
        ce('button', {onClick: e => this.props.goToPublic(), className: "goToRecordings"}, 'Enter Public Lobby'),
        ce('br'),
        ce('button', {onClick: e => this.props.goToRecordings(), className: "goToRecordings"}, 'See Recording Feed'),
        ce('br'),
        ce('button', {onClick: e => this.props.goToInstruments(), className: "goToRecordings"}, 'Instrument Workshop'),
        ce('br'),

        ce('button', { onClick: e => this.props.doLogout(), className: "logOut" }, 'Log out')
      ),


      );

  }

  searchForProject() {
    this.enterProject(1)
  }

  handleProjectSearch(e) {
    this.state.searchBar = e.target.value
    console.log(this.state.searchBar)
  }

  enterProject(id) {
        //ce(ProjectPageComponent)
        console.log(id.target.value)
            projectId = id.target.value;

            this.props.goToProject()

  }

  goToPublicPage() {
    console.log("going to pub page")
    ce(PublicPageComponent)
  }

  loadTasks() {

    fetch(tasksRoute).then(res => res.json()).then(projects => {
      //var parsed = projects

      /*for(let i=0;i<projects.length;i++) {
        projects[i].text = projects[i].text.split("####")[0]
      }*/

      this.setState({ projects })

    });


    //ce('br')



  }

  handleChange(e) {
    this.setState({newProject: e.target.value})

  }
  handleSelectChange(e) {
    this.setState({userSelected: e.target.value})
  }


  handlePubChange(e) {
    this.setState({newPub: e.target.value})
  }
  handleAddClick(e) {
    fetch(addRoute, {
        method: 'POST',
        headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
        body: JSON.stringify(this.state.newProject + "####" + this.state.userSelected)//)
    }).then(res => res.json()).then(data => {
        if(data) {
            this.loadTasks();
            //document.getElementById("newTask").value = "";
            //document.getElementById("task-message").innerHTML = "";
            this.setState({ taskMessage: "", newProject: "" });

        } else {
             //TODO
            this.setState({ taskMessage: "Failed to send message" });

        }
    });
  }
  handlePubClick(e) {
    fetch(pubRoute, {
        method: 'POST',
        headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
        body: JSON.stringify(this.state.newPub)//)
    }).then(res => res.json()).then(data => {
        if(data) {
            this.loadPublics();
            this.setState({ taskMessage: "", newPub: "" });

            //document.getElementById("newTask").value = "";
            //document.getElementById("task-message").innerHTML = "";
        } else {
             //TODO
            //document.getElementById("task-message").innerHTML = "Failed to send message";

        }
    });
  }

  handleDeleteClick(i) {
    fetch(deleteRoute, {
      method: 'POST',
      headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
      body: JSON.stringify(i + "####" + "Private")
      }).then(res => res.json()).then(data => {
        if(data) {
          this.loadTasks();
          this.setState({ taskMessage: "" })
        } else {
                     //TODO
          this.setState({ taskMessage: "Failed to delete" })
        }
    });
  }

  handleDeletePublic(i) {
    fetch(deleteRoute, {
      method: 'POST',
      headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
      body: JSON.stringify(i + "####" + "Public")
      }).then(res => res.json()).then(data => {
        if(data) {
          this.loadPublics();
          this.setState({ taskMessage: "" })
        } else {
                     //TODO
          this.setState({ taskMessage: "Failed to delete" })
        }
    });
  }



}


class PublicPageComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state = { publics: [] };
    //this.state = { publics: [] };
  }

  componentDidMount() {
    this.loadPublics();
    //this.setState({ userSelected: usersAvailable[0] });
  }


  render() {
    return ce('div', null,
      ce('h2', {className: "font"}, 'Public Projects'),
      ce('table', {className: "ppTable"},
        ce('thead', {className: "rowsPT"}, ce('tr', {className: "rowsPT"}, ce('th', {className: "font"}, "Project Name"), ce('th', {className: "font"}, "Owner"))),
          ce('tbody', {className: "rowsPT"}, this.state.publics.map(task => ce('tr', { className: "rowsPT", key: task.id, onClick: e => this.enterProject(task.id) }, ce('td', {className: "font"}, task.text), ce('td', {className: "font"}, task.id))


            ))


      ),

      ce('br'),
        ce('button', { onClick: e => this.props.goToHome(), className: "homeBtn" }, 'Home')

      );
  }

  findOwner(id) {

  }

  enterProject(id) {
    console.log("entering project: " + id)
    //ce(ProjectPageComponent)
    this.props.goToProject()
  }

  loadPublics() {
    fetch(publicsRoute).then(res => res.json()).then(publics => this.setState({ publics }));

  }

  openPublicProject() {
    console.log("project opened")
  }

}

class ProjectPageComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      midinotes: "",
      instruments: []
    };
  }

  componentDidMount() {

    this.loadMidiData();
    this.loadInstruments();

  }
  render() {
    canv.style.display = "block"

    return ce('div', {className: "projSeq"},
      'Project Sequencer',

      ce('h2', {className: "yourProject"}, 'Your Project'),
      ce('h2', {className: "instrument"}, 'Instruments: '),
      ce('select', {onChange: e => this.loadInstrument(e)},
          this.state.instruments.map(inst => ce('option', { key: inst.id }, inst.instrumentName))
        //ce('option', 0, "Tst")
        ),
      ce('br'),
      ce('button', { onClick: e => this.homePressed(e), className: "homebtn" }, 'Home')

      )
  }

  loadMidiData() {
    /*console.log("loading midi data")
    fetch(notesFromDb).then(res => res.json()).then(instruments => {
      var tmp = instruments.split(",")
      var parsed = []
      for(let i=0; i<tmp.length(); i++) {
        if(i%2===0) {
          parsed.push([tmp[i-1], tmp[i]])
        }
      }
      indexesOfReds = parsed
    })*/
    getNotes(projectId)
  }
  handleSelectChange(e) {
    console.log("handling select")
  }

  loadInstrument(id) {
    console.log("Instrument " + id.target.value + " is playing")
    //Get song by id here
    fetch(loadInstrumentPath, {
        method: 'POST',
        headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
        body: JSON.stringify(id.target.value)
    }).then(res => res.json()).then(data => {
        if(data) {
          console.log("uploaded")
        } else {
          console.log("not uploaded")
        }
          /*if(this.hasSong) {
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



  homePressed(e) {
    canv.style.display = "none"
    this.props.goToHome()
  }

  loadInstruments() {
    console.log("Loading instruments")
    fetch(instrumentsList).then(res => res.json()).then(instruments => this.setState({ instruments }));


  }

}

class InstrumentsPageComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      instruments: []
    };
  }

  componentDidMount() {
    this.loadInstruments()
  }

  render() {
    uploadInstrumentDiv.style.display = "block"


    return ce('div', null,
      'Instruments Workshop',
            ce('table', null,
        ce('thead', null, ce('tr', null, ce('th', null, "Instrument Name"), ce('th', null, "Description"))),
          ce('tbody', null, this.state.instruments.map(inst => ce('tr', { key: inst.id, onClick: e => this.playSong(inst.id) }, ce('td', null, inst.instrumentName), ce('td', null, inst.description))
            )
            )
          ),


      //ce('button', { onClick: e => this.homePressed(e) }, 'Upload a recording'),

      ce('button', { onClick: e => this.homePressed(e) }, 'Home'),
      ce('h2', null, 'Upload an instrument')

      );
  }
  playSong() {
    console.log("this should actually not do anything")
  }

  loadInstruments() {
    console.log("Loading instruments")
    fetch(instrumentsList).then(res => res.json()).then(instruments => this.setState({ instruments }));


  }

  homePressed(e) {
    uploadInstrumentDiv.style.display = "none"
    this.props.goToHome()
  }



}

class RecordingsPageComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isLoading: true,
      recordings: [],
      hasSong: false
    };
  }

  componentDidMount() {
    this.showLoading();
    this.loadRecordings();
    //this.stopShowingLoading();
  }
  render() {
    uploadDiv.style.display = "block"


    return ce('div', null,
      'Recording Feed',

            ce('table', null,
        ce('thead', null, ce('tr', null, ce('th', null, "Recording Name"), ce('th', null, "Description"))),
          ce('tbody', null, this.state.recordings.map(rec => ce('tr', { key: rec.id, onClick: e => this.playSong(rec.id) }, ce('td', null, rec.name), ce('td', null, rec.description))
            )
            )
          ),
      ce('img', { id: "loader", src: loadingGif }, null),
      //ce('button', { onClick: e => this.homePressed(e) }, 'Upload a recording'),
      ce('button', { onClick: e => this.homePressed(e) }, 'Home'),
      ce('h2', null, 'Upload a Recording')

      );
  }
  showLoading() {
    //ce('img', { id: "loader", src: loadingGif }, null)

    //document.getElementById("react-root").appendChild(loadingGif)
  }
  stopShowingLoading() {
    console.log("stop loading")
    document.getElementById("loader").style.display = "none"
  }

  loadRecordings() {
    fetch(recordingsList).then(res => res.json()).then(recordings => { this.stopShowingLoading(); this.setState({ recordings } )});
    //this.stopShowingLoading()
  }

  playSong(id) {
    console.log("song " + id + " is playing")
    //Get song by id here
    fetch(playRecording, {
        method: 'POST',
        headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
        body: JSON.stringify(id.toString())
    }).then(res => res.json()).then(data => {
        if(data) {
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
        }
    });

    //Play song:

  }

  homePressed(e) {
    uploadDiv.style.display = "none"
    this.setState( { hasSong: false } );
    this.props.goToHome()
  }

}


ReactDOM.render(
  ce(OpenMicMainComponent, null, null),
  document.getElementById('react-root')
  );
