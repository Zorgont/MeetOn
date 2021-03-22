import React, { Component } from "react";
import MeetingService from "../services/MeetingService";
import 'font-awesome/css/font-awesome.min.css';
export default class MeetingPage extends Component{
    constructor(props) {
        console.log("constructor");
        super(props);
        
        this.state = {
            meeting: {}
        };
        
    }

    componentDidMount() {
        console.log("here");
        MeetingService.getMeetingById(this.props.match.params.id).then((res) => {
            this.setState({meeting: res.data});
            console.log(this.state.meeting);
        });
    }
    
    render() {
        console.log(this.props.match.params.id);
        return (
            <div>   
                <div className="container">
                    <div className="row">
                        <div className="card col-md-8 offset-md-2 offset-md-2">
                            <div className="card-body">
                                <div className="row">
                                    <h2>{this.state.meeting.name}</h2>
                                </div>
                                <div className="row">
                                    <p>About meeting:</p>
                                    <p>{this.state.meeting.about}</p>
                                </div>
                                <div className="row">
                                    <p> Date: </p>
                                    <p> {this.state.meeting.date} </p>
                                </div>
                                <div className="row">
                                    <p> Participant amount: {this.state.meeting.participantAmount}</p>
                                </div>
                                <div className="row">
                                    <p> Participant amount restricted? {this.state.meeting.isParticipantAmountRestricted}</p>
                                </div>
                                <div className="row">
                                    <p> Is private?: {this.state.meeting.isPrivate} </p>
                                </div>
                                <div className="row">
                                    <p> Details: </p>
                                    <p>{this.state.meeting.details}</p>
                                </div>
                                <div className="row">
                                {
                                    this.state.meeting?.tags?.map(
                                        tag => 
                                            <div style={{marginRight: "10px", background: "#ddd", padding: 3, borderRadius: "10px"}}> {tag} </div>
                                    )
                                }                     
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        );
    }
}