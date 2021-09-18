import React, { Component } from "react";
import { Redirect } from "react-router-dom";
import AuthService from "../../Services/AuthService";
//import { Header } from 'react-native-elements';
import {Button, Container, Row, Col, Label} from "reactstrap";
import '../../App.css'
import axios from "axios";
import { API_URL } from "../../constant";

const iconStyle = {
	marginTop: 40,
	
	fontSize: 100
}

export default class ChangeIcon extends Component{

  constructor(props){
    super(props);
    this.state = {
      chosen: "",
      redirect: null,
      userReady: false,
      currentUser: AuthService.getCurrentUser(),
      basic: localStorage.getItem("basic"),
      userID: JSON.parse(localStorage.getItem("user")).id
    }

    this.handleSubmit = this.handleSubmit.bind(this);
    this.changeIcon = this.changeIcon.bind(this);
  }

  componentDidMount() {
    if (!this.state.currentUser) this.setState({ redirect: "/home" });

  }


  async changeIcon(newIcon) {
    const user = AuthService.getBasicInfo();
    if (user && user.token) {
      const token = user.token;
      
      //newIcon = JSON.stringify(newIcon);
      //console.log(newIcon);
      const response = await axios.post (
        API_URL + "/user/changeIcon", 
        {
          icon: newIcon
        },
        {
          headers: {
            Authorization: "Bearer " + token,
          },
        }
      );
      console.log(response.data);
      console.log(this.state.currentUser)
      
      return response.data;
    
    } else {
      return "An error has occured"
    }
  }

  
  handleClick(newIcon) {
    this.setState({chosen: newIcon});
    //console.log(this.state.chosen);
  }

  async handleSubmit() {
    let basic = AuthService.getBasicInfo();
    
    let newIcon = await this.changeIcon(this.state.chosen);
    console.log(this.newIcon);
    if (newIcon !== "An error has occured") {
      alert("Changes saved!");

    } else {
      alert(newIcon);
    }

    await AuthService.getUserDataFromBackend(basic.token, basic.id);
  }

  render() {

    const {redirect} = this.state;
    if (redirect) {
      return <Redirect to={this.state.redirect} />
    }
    return (
      
      <Container>
        <Row>
          <Col>
            <Button 
              className = "change-icon-btn-frame" 
              onClick={() => this.handleClick("fas fa-blind")}
            >
              <i  className="fas fa-blind" style={iconStyle}></i>
            </Button>
            
          </Col>
          <Col>
            <Button 
              className = "change-icon-btn-frame"
              onClick={() => this.handleClick("fa fa-user fa-fw")}
            >
              <i  className="fa fa-user fa-fw" style={iconStyle}></i>
            </Button>

          </Col>
          <Col>
            <Button 
              className = "change-icon-btn-frame"
              onClick={() => this.handleClick("	fas fa-wheelchair")}
            >
              <i  className="	fas fa-wheelchair" style={iconStyle}></i>
            </Button>
          </Col>
          <Col>
            <Button 
              className = "change-icon-btn-frame"
              onClick={() => this.handleClick("	fab fa-accessible-icon")}
            >
              <i  className="	fab fa-accessible-icon" style={iconStyle}></i>
            </Button>
          </Col>
          <Col>
            <Button 
              className = "change-icon-btn-frame"
              onClick={() => this.handleClick("	far fa-question-circle")}
            >
              <i  className="	far fa-question-circle" style={iconStyle}></i>
            </Button>
          </Col>
        </Row>
        <Row className="change-icon-line">
          <Col></Col>
          <Col xs="7">
            <Button 
              className = "change-icon-btn-save"
              onClick={this.handleSubmit}
            >
              Save changes
            </Button>
          </Col>
          
          
        </Row>
        
      </Container>

      
    )
  }
}