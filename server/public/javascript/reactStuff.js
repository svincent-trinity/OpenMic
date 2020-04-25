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


const canv = document.getElementById("sequencerCanvas");
canv.style.display = "none"

class OpenMicMainComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state = { loggedIn: false, pageOn: "home" };
  }



  render() {
    if(this.state.loggedIn) {
      if(this.state.pageOn == "home") {
        console.log("home")
        return ce(HomePageComponent, { goToProject: () => this.setState( {pageOn: "project"} ), goToPublic: () => this.setState( {pageOn: "public"} ), doLogout: () => this.setState( {loggedIn: false, pageOn: "home"})});
      } else if (this.state.pageOn == "public") {
        console.log("public")
        return ce(PublicPageComponent, { goToProject: () => this.setState( {pageOn: "project"} ), goToHome: () => this.setState( {pageOn: "home"} ), doLogout: () => this.setState( {loggedIn: false, pageOn: "home"})});

      } else if (this.state.pageOn == "project") {
        console.log("project")
        return ce(ProjectPageComponent, { goToHome: () => this.setState( {pageOn: "home"} ), doLogout: () => this.setState( {loggedIn: false, pageOn: "home"})});

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
		return ce('div', null,
        ce('h2', null, 'Login: '),
        ce('br'),
        'Username: ',
        ce('input', {type: "text", id: "loginName", value: this.state.loginName, onChange: e=> this.changeHandler(e)}),
        ce('br'),
        'Password: ',
        ce('input', {type: "password", id: "loginPass", value: this.state.loginPass, onChange: e=> this.changeHandler(e)}),
        ce('br'),
        ce('button', {onClick: e => this.login(e)}, 'Login'),
        ce('span', {id: "login-message"}, this.state.loginMessage),
        ce('h2', null, 'Create User:'),
        ce('br'),
        'Username: ',
        ce('input', {type: "text", id: "createName", value: this.state.createName, onChange: e=> this.changeHandler(e)}),
        ce('br'),
        'Password: ',
        ce('input', {type: "password", id: "createPass", value: this.state.createPass, onChange: e=> this.changeHandler(e)}),
        ce('br'),
        ce('button', {onClick: e => this.createUser(e)}, 'Create User'),
        ce('span', {id: "create-message"}, this.state.createMessage)

	);
	}


	changeHandler(e) {
    this.setState({ [e.target['id']]: e.target.value });
	}

  login(e) {
  const username = this.state.loginName;
  const password = this.state.loginPass;
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
    });
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
    this.state = { projects: [], newProject: "", isPublic: ["Private", "Public"], userSelected: "Private", projectName: "" };
  }
  
  componentDidMount() {
    this.loadTasks();
  }


  render() {
    return ce('div', null, 
      'OpenMic',
      ce('h2', null, 'My Projects'),
      ce('br'),
      ce('table', null, 
        ce('thead', null, ce('tr', null, ce('th', null, "Project Name"), ce('th', null, "Privacy"))),
          ce('tbody', null, this.state.projects.map(task => ce('tr', { key: task.id, onClick: e => this.props.goToProject() }, ce('td', null, task.text), ce('td', null, task.isPublic))
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
          )),

      
      ce('br'),

      ce('button', {onClick: e => this.props.goToPublic()}, 'Enter Public Lobby'),

      ce('br'),
      ce('button', { onClick: e => this.props.doLogout() }, 'Log out')
      );
  }

  enterProject(id) {
        ce(ProjectPageComponent)
            this.props.goToProject


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
      'Public Projects',

      ce('h2', null, 'Public Projects'),
      ce('table', null,
        ce('thead', null, ce('tr', null, ce('th', null, "Project Name"), ce('th', null, "Owner"))),
          ce('tbody', null, this.state.publics.map(task => ce('tr', { key: task.id, onClick: e => this.enterProject(task.id) }, ce('td', null, task.text), ce('td', null, task.id))

            ))


      ),
 
      ce('br'),
      ce('button', { onClick: e => this.props.goToHome() }, 'Home')
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
      midinotes: ""
    };
  }

  componentDidMount() {
    this.loadMidiData();
  }
  render() {
    canv.style.display = "block"

    return ce('div', null, 
      'Project Sequencer',
      
      ce('h2', null, 'Your Project'),
      ce('button', { onClick: e => this.homePressed(e) }, 'Home')
      );
  }

  loadMidiData() {
    console.log("loading midi data")
  }


  homePressed(e) {
    canv.style.display = "none"
    this.props.goToHome()
  }

}


ReactDOM.render(
  ce(OpenMicMainComponent, null, null),
  document.getElementById('react-root')
  );
