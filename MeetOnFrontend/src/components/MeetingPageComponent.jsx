import React, { Component } from "react";
import MeetingService from "../services/MeetingService";
import 'font-awesome/css/font-awesome.min.css';
import AuthService from "../services/AuthService";
import RequestService from "../services/RequestService";
import CommentService from "../services/CommentService";
import CommentsList from "./CommentsListComponent";
import { Link } from 'react-router-dom';



export default class MeetingPage extends Component{
    constructor(props) {
        super(props);
        
        this.state = {
            meeting: {},
            request: null,
            comments: [],
            currentUser: AuthService.getCurrentUser(),
            requestsAmount: 0
        };
    }

    componentDidMount() {
        MeetingService.getMeetingById(this.props.match.params.id).then((res) => {
            this.setState({ meeting: res.data});

            CommentService.getCommentsByMeetingId(this.state.meeting.meetingId).then((res) => {
                this.setState({ comments: res.data});
            });

            RequestService.getAprovedRequestsAmount(this.state.meeting.meetingId).then((res) => {
                this.setState({ requestsAmount: res.data});
            });
            
            RequestService.getRequestByMeetingAndUser(this.state.meeting.meetingId, this.state.currentUser.id).then((res) => {
                this.setState({ request: res.data});
            });


            console.log(this.state)
        });

    }
    commentsList(){
        if(this.state.meeting.status==="FINISHED") {

            return  <CommentsList comments={this.state.comments} onCommentChange={this.addComment.bind(this)}/>

        }
    }
    addComment(content){
        const comment={
            meeting_id:this.state.meeting.meetingId,
            meetingName:this.state.meeting.name,
            user_id:this.state.currentUser.id,
            username:this.state.currentUser.username,
            content:content
        }
        console.log(comment)
        CommentService.createComment(comment).then(r => {
            CommentService.getCommentsByMeetingId(this.state.meeting.meetingId).then((res) => {
                this.setState({ comments: res.data});
            })
            }
        )
    }

    deleteMeeting() {
        MeetingService.deleteMeeting(this.props.match.params.id).then((res) => {
            this.props.history.push('/meetings');
        });
    }
    buttonDelete() {
        if (this.state.meeting.managerId === AuthService.getCurrentUser().id)
            return  <div>     
                        <button className="btn btn-danger" style={{marginLeft:"5px"}} onClick={this.deleteMeeting.bind(this)}>Delete</button>
                    </div>
    }
    buttonUpdate() {
        if (this.state.meeting.managerId === AuthService.getCurrentUser().id)
            return <div>
                        <Link to={`/update/${this.props.match.params.id}`}>
                            <button className="btn btn-primary">Update</button>
                        </Link>
                    </div>
    }
    buttonEnroll() {
        if (this.state.meeting.managerId !== AuthService.getCurrentUser().id){
            if(this.state.request)
                return  <div>
                            <p>You have already enrolled to the meeting!</p>
                            <p>Status: {this.state.request.status}</p>
                        </div>
            if(this.state.meeting.isParticipantAmountRestricted)
            if(this.state.requestsAmount >= this.state.meeting.participantAmount)
                return <div><p>No available places!</p></div>

            return  <div>
                        <Link to={`/enroll/${this.props.match.params.id}`}>
                            <button className="btn btn-primary" style={{marginLeft:"5px"}}>Enroll</button>
                        </Link>
                    </div>
        }
    }

    render() {
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
                                    <p> Beginning Date: </p>
                                    <p> {this.state.meeting.date} </p>
                                </div>
                                <div className="row">
                                    <p> End Date: </p>
                                    <p> {this.state.meeting.endDate} </p>
                                </div>
                                <div className="row">
                                    <p> Participant amount: {this.state.meeting.participantAmount}</p>
                                </div>
                                <div className="row">
                                    <p> Participant amount restricted? {this.state.meeting.isParticipantAmountRestricted? "true" : "false"}</p>
                                </div>
                                <div className="row">
                                    <p> Is private?: {this.state.meeting.isPrivate? "true" : "false"} </p>
                                </div>
                                <div className="row">
                                    <p> Details: </p>
                                    <p>{this.state.meeting.details}</p>
                                </div>
                                <div className="row">
                                {
                                    this.state.meeting?.tags?.map(
                                        tag => 
                                            <div style={{marginRight: "10px", marginBottom: "10px",background: "#ddd", padding: 3, borderRadius: "10px"}}> {tag} </div>
                                    )
                                }                     
                                </div>
                                <div className="row"> 
                                    {this.buttonUpdate()}
                                    {this.buttonDelete()}
                                    {this.buttonEnroll()}
                                </div>
                                {this.commentsList()}
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        );
    }
}