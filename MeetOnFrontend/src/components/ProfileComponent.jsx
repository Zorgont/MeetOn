import React, { Component } from "react";
import { Link } from 'react-router-dom';
import AuthService from "../services/AuthService";
import MeetingService from "../services/MeetingService";

export default class Profile extends Component {
    constructor(props) {
        super(props);

        this.state = {
            currentUser: AuthService.getCurrentUser(),
            meetings: []
        };
    }
    
    componentDidMount() {
        MeetingService.getMeetingsByManager(this.state.currentUser.username).then((res) => {
            this.setState({meetings: res.data});
        });
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
                <button className="btn btn-primary" onClick={this.createMeeting.bind(this)}>Create meeting</button>
                <h2 className="text-center">Meetings list</h2>
                        <table className="table table-striped table-bordered">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>About</th>
                                    <th>Requests</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    this.state.meetings.map(
                                        meeting => 
                                        <tr key={meeting.id}>
                                            <td><Link to={`/meetings/${meeting.meetingId}`}>{meeting.name}</Link></td>
                                            <td> {meeting.about} </td>
                                            <td> <a href="#">Requests</a> </td>

                                        </tr>
                                    )
                                }
                            </tbody>
                        </table>
            </div>
        );
    }
}