import React, { Component } from "react";
import AuthService from "../services/AuthService";
import MeetingService from "../services/MeetingService";
import 'font-awesome/css/font-awesome.min.css';
export default class CreateMeeting extends Component{
    constructor(props) {
        super(props);

        this.state = {
            currentUser: AuthService.getCurrentUser(),
            name: "",
            about: "",
            date: "",
            isParticipantAmountRestricted: '',
            participantAmount: 0,
            isPrivate: '',
            details: "",
            tags: []
        };

    }
    updateMeeting = (event) => {
        event.preventDefault();
        let meeting = {
            managerUsername: this.state.currentUser.username,
            name: this.state.name,
            about: this.state.about,
            date: this.state.date,
            isParticipantAmountRestricted : this.state.isParticipantAmountRestricted ,
            participantAmount: this.state.participantAmount,
            isPrivate: this.state.isPrivate,
            details: this.state.details,
            tags: this.state.tags
        };
        console.log(meeting);
        MeetingService.updateMeeting(meeting,this.props.match.params.id).then(res => {
            this.props.history.push('/meetings');
        });
    }

    changeNameHandler = (event) => {
        this.setState({name: event.target.value});
        console.log(event.target.value);
    }

    changeAboutHandler = (event) => {
        this.setState({about: event.target.value});
        console.log(event.target.value);
    }
    changeDateHandler = (event) => {
        this.setState({date: event.target.value});
        console.log(event.target.value);
    }
    changeParticipantAmountHandler = (event) => {
        this.setState({participantAmount: event.target.value});
        console.log(event.target.value);
    }
    changeIsParticipantAmountRestrictedHandler = (event) => {
        this.setState({isParticipantAmountRestricted: event.target.checked});
        console.log(event.target.checked);
    }
    changePrivateHandler = (event) => {
        this.setState({isPrivate: event.target.checked});
        console.log(event.target.checked);
    }
    changeDetailsHandler = (event) => {
        this.setState({details: event.target.value});
        console.log(event.target.value);
    }
    cancel() {
        this.props.history.push('/profile');
    }
    addTag(event) {
        let newValue = document.getElementById('newTagName').value;
        document.getElementById('newTagName').value='';

        if (!newValue ||this.state.tags.includes(newValue))
            return;

        this.setState({tags: this.state.tags.concat(newValue)});
    }
    removeTag(removeValue, event) {
        this.state.tags.splice(this.state.tags.indexOf(removeValue), 1);
        this.setState({tags: this.state.tags});
    }
    componentDidMount() {
        MeetingService.getMeetingById(this.props.match.params.id).then(res => {
            let meeting = res.data;
            this.setState(
                {
                    name: meeting.name,
                    about: meeting.about,
                    date: meeting.date.substring(0,16),
                    isParticipantAmountRestricted : meeting.isParticipantAmountRestricted ,
                    participantAmount: parseInt(meeting.participantAmount),
                    isPrivate: meeting.isPrivate ,
                    details: meeting.details,
                    tags: meeting.tags
                });
            console.log(this.state);
        })
    }

    render() {
        return (
            <div>
                <div className="container">
                    <div className="row">
                        <div className="card col-md-6 offset-md-3 offset-md-3">
                            <h3 className="text-center">Update meeting</h3>
                            <div className="card-body">
                                <form>
                                    <div className="form-group row">
                                        <label for="name"> Meeting name: </label>
                                        <input type="text" name="name" className="form-control" value={this.state.name} onChange={this.changeNameHandler.bind(this)} required />
                                    </div>
                                    <div className="form-group row">
                                        <label for="about"> About: </label>
                                        <input type="text"  name="about" className="form-control" value={this.state.about} onChange={this.changeAboutHandler.bind(this)} required />
                                    </div>
                                    <div className="form-group row">
                                        <label htmlFor="date"> Date: </label>
                                        <input type="datetime-local" name="date" className="form-control" value={this.state.date} onChange={this.changeDateHandler.bind(this)} required/>
                                    </div>
                                    <div className="form-group row">
                                        <label htmlFor="participant_amount"> ParticipantAmount: </label>
                                        <input type="number" name="participant_amount" className="form-control" value={this.state.participantAmount} onChange={this.changeParticipantAmountHandler.bind(this)} required/>
                                    </div>
                                    <div className="form-group row">
                                        <label htmlFor="is_participant_amount_restricted"> Participant amount restricted? </label>
                                        <input type="checkbox" name="is_participant_amount_restricted" className="form-control" checked={this.state.isParticipantAmountRestricted} onChange={this.changeIsParticipantAmountRestrictedHandler.bind(this)} required/>
                                    </div>
                                    <div className="form-group row">
                                        <label htmlFor="isPrivate"> Is private?: </label>
                                        <input type="checkbox" name="isPrivate" className="form-control" checked={this.state.isPrivate} onChange={this.changePrivateHandler.bind(this)}/>
                                    </div>
                                    <div className="form-group row">
                                        <label htmlFor="details"> Details: </label>
                                        <input type="text" name="details" className="form-control" value={this.state.details} onChange={this.changeDetailsHandler.bind(this)} required/>
                                    </div>
                                    <div className="form-group row">
                                        <label htmlFor="tags"> Tags </label>
                                    </div>
                                    <div className="row" style={{background: "white", borderRadius: "5px", padding: 5, marginBottom: 15, borderColor: "black", border: "2px solid #d5d5d5"}}>
                                        {
                                            this.state.tags.map(
                                                tag =>
                                                    <div style={{marginRight: "10px", background: "#ddd", padding: 3, borderRadius: "10px"}}> {tag} <i className="fa fa-times" value={tag} onClick={this.removeTag.bind(this, tag)}></i></div>
                                            )
                                        }
                                    </div>
                                    <div className="row">
                                        <input id="newTagName" type="text" name="addTag" className="form-control col-9"/>
                                        <input type="button" className="btn btn-secondary col-3" onClick={this.addTag.bind(this)} value="Add"/>
                                    </div>
                                    <div className="row">
                                        <button className="btn btn-success" onClick={this.updateMeeting.bind(this)}>Save</button>
                                        <button className="btn btn-danger" onClick={this.cancel.bind(this)} style={{marginLeft: "10px"}}>Cancel</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        );
    }
}