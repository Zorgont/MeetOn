import React, { Component } from "react";
import AuthService from "../services/AuthService";


export default class Profile extends Component {
    constructor(props) {
        super(props);

        this.state = {
            currentUser: AuthService.getCurrentUser()
        };
    }
    createMeeting(e){
        this.props.history.push('/add_meeting');

    }
    render() {
        const { currentUser } = this.state;

        console.log(this.state.currentUser);

        return (
            <div className="container">
                <header className="jumbotron">
                    <h3>
                        <strong>{currentUser.username}</strong> Profile
                    </h3>
                </header>
                <p>
                    <strong>Token:</strong>{" "}
                    {currentUser.token}{" "}
                </p>
                <p>
                    <strong>Id:</strong>{" "}
                    {currentUser.id}
                </p>
                <p>
                    <strong>Email:</strong>{" "}
                    {currentUser.email}
                </p>
                <strong>Authorities:</strong>
                <ul>
                    {currentUser.roles &&
                    currentUser.roles.map((role, index) => <li key={index}>{role}</li>)}
                </ul>
                <button className="btn btn-primary" onClick={this.createMeeting.bind(this)}/>
            </div>
        );
    }
}