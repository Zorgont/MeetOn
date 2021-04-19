import React, { Component } from "react";
import { Link } from 'react-router-dom';
import AuthService from "../services/AuthService";
import MeetingService from "../services/MeetingService";
import RequestService from "../services/RequestService";
import NotificationService from "../services/NotificationService";

export default class Profile extends Component {
    constructor(props) {
        super(props);

        this.state = {
            currentUser: AuthService.getCurrentUser(),
            meetings: [],
            requests:[],
            notifications: []
        };
    }
    
    componentDidMount() {
        this.updateTables();
        console.log("current user id: " + this.state.currentUser.id);
        NotificationService.getNotificationsByUser(this.state.currentUser.id).then((res) => {
            this.setState({notifications: res.data});
        })
    }

    updateTables() {
        MeetingService.getMeetingsByManager(this.state.currentUser.username).then((res) => {
            this.setState({meetings: res.data});
            RequestService.getRequestsByUserId(this.state.currentUser.id).then((res)=>{
                this.setState({requests: res.data})
            })
        });
    }    
    
    createMeeting(e){
        this.props.history.push('/add_meeting');
    }

    removeRequest(id){
        RequestService.removeRequest(id).then((res) => {
            this.updateTables();
        })
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
                <Link to="/profile/update">
                    <button className="btn btn-primary">Profile settings</button>
                </Link>
                <div className="row">
                    <div className="col-6">
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
                                            <td><Link to={`/meetings/${meeting.meetingId}/requests`}>Requests</Link></td>
                                        </tr>
                                    )
                                }
                            </tbody>
                        </table>
                    </div>
                    <div className="col-6">
                        <h2 className="text-center">Requests list</h2>
                        <table className="table table-striped table-bordered">
                            <thead>
                                <tr>
                                    <th>Meeting</th>
                                    <th>About</th>
                                    <th>Status</th>
                                    <th>Delete</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    this.state.requests.map(
                                        request => 
                                        <tr key={request.id}>
                                            <td><Link to={`/meetings/${request.meeting_id}`}>{request.meetingName}</Link></td>
                                            <td> {request.about} </td>
                                            <td> {request.status} </td>
                                            <td>
                                                <button className="btn btn-danger" onClick={this.removeRequest.bind(this, request.id)}>Delete</button>
                                            </td>
                                        </tr>
                                    )
                                }
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

        );
    }
}