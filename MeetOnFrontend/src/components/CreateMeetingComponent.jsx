import React, { Component } from "react";
import AuthService from "../services/AuthService";
import MeetingService from "../services/MeetingService";
export default class  CreateMeeting extends Component{
    constructor(props) {
        super(props);

        this.state = {
            currentUser: AuthService.getCurrentUser(),
            name:"",
            about:"",
            date:"",
            participant_amount:"0",
            is_private:"",
            details:""
        };
    }
    saveMeeting = (event) => {
        event.preventDefault();
        let meeting = {
            manager: this.state.currentUser,
            name: this.state.name,
            about: this.state.about ,
            date: this.state.date,
            participantAmount:this.state.participant_amount,
            isPrivate: this.state.is_private,
            details: this.state.details};
            MeetingService.createMeeting(meeting).then(res => {
            this.props.history.push('/profile');
        });
    }

    changeNameHandler = (event) => {
        this.setState({name: event.target.value});
    }

    changeAboutHandler = (event) => {
        this.setState({about: event.target.value});
    }
    changeDateHandler = (event) => {
        this.setState({date: event.target.value});
    }
    changeParticipantAmountHandler = (event) => {
        this.setState({participant_amount: event.target.value});
    }
    changePrivateHandler = (event) => {
        this.setState({is_private: event.target.value});
    }
    changeDetailsHandler = (event) => {
        this.setState({details: event.target.value});
    }
    cancel() {
        this.props.history.push('/users');
    }

    render() {
        return (
            <div>
                <div className="container">
                    <div className="row">
                        <div className="card col-md-6 offset-md-3 offset-md-3">
                            <h3 className="text-center">Add User</h3>
                            <div className="card-body">
                                <form>
                                    <div className="form-group">
                                        <label for="name"> Meeting name: </label>
                                        <input type="text" name="name" className="form-control" value={this.state.name} onChange={this.changeNameHandler.bind(this)} required />
                                    </div>
                                    <div className="form-group">
                                        <label for="about"> About: </label>
                                        <input type="text"  name="about" className="form-control" value={this.state.about} onChange={this.changeAboutHandler.bind(this)} required />
                                    </div>
                                    <div className="form-group">
                                        <label htmlFor="date"> Date: </label>
                                        <input type="date" name="date" className="form-control" value={this.state.date} onChange={this.changeDateHandler.bind(this)} required/>
                                    </div>
                                    <div className="form-group">
                                        <label htmlFor="participant_amount"> ParticipantAmount: </label>
                                        <input type="number" name="participant_amount" className="form-control" value={this.state.participant_amount} onChange={this.changeParticipantAmountHandler.bind(this)} required/>
                                    </div>
                                    <div className="form-group">
                                        <label htmlFor="is_private"> Is_private?: </label>
                                        <input type="checkbox" name="is_private" className="form-control" value={this.state.is_private} onChange={this.changePrivateHandler.bind(this)} required/>
                                    </div>
                                    <div className="form-group">
                                        <label htmlFor="details"> Details: </label>
                                        <input type="text"  name="details" className="form-control" value={this.state.details} onChange={this.changeDetailsHandler.bind(this)} required/>
                                    </div>

                                    <button className="btn btn-success" onClick={this.saveMeeting.bind(this)}>Save</button>
                                    <button className="btn btn-danger" onClick={this.cancel.bind(this)} style={{marginLeft: "10px"}}>Cancel</button>
                                </form>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        );
    }

}