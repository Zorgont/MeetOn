import React, { Component } from "react";
import MeetingService from "../services/MeetingService";
import 'font-awesome/css/font-awesome.min.css';
import AuthService from "../services/AuthService";
import RequestService from "../services/RequestService";
import CommentService from "../services/CommentService";
import CommentsList from "./CommentsListComponent";
import { Link } from 'react-router-dom';
import SockJsClient from "react-stomp";
import PlatformService from "../services/PlatformService";
import MeetingRating from "./MeetingRatingComponent";
import ScoreService from "../services/ScoreService";
import {Box} from "@material-ui/core";
import {Rating} from "@material-ui/lab";

export default class MeetingPage extends Component{
    constructor(props) {
        super(props);

        this.state = {
            meeting: {},
            request: null,
            comments: [],
            currentUser: AuthService.getCurrentUser(),
            requestsAmount: 0,
            platforms: [],
            currentRating: 0,
            userRating: 0
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

            RequestService.getRequestByMeetingAndUser(this.state.meeting.meetingId, this.state.currentUser?.id).then((res) => {
                this.setState({ request: res.data});
                console.log(this.state.request)
            });

            PlatformService.getAllPlatforms().then(res => {
                this.setState({platforms: res.data});
                for(let platform of this.state.meeting.meetingPlatforms) {
                    let name = this.state.platforms.find(plat => plat.id === platform.platformId).name;
                    platform.name = name;
                    platform.meetingId = this.state.meeting.meetingId;
                }
                this.setState({meeting: this.state.meeting});
                console.log(this.state)
            });

            this.updateMeetingRating();
            ScoreService.getUserScore(this.state.meeting.meetingId, this.state.currentUser?.id).then( (res) => {

                    this.setState({
                        userRating: res.data.score ? res.data.score : 0
                    })
                console.log(this.state.userRating)
                }
            )

            console.log(`/meeting/${this.state.meeting.meetingId}/queue/comments`)
        });

    }

    updateMeetingRating = () => {
        ScoreService.getAggregatedScore(this.state.meeting.meetingId).then( (res) => {
                this.setState({
                    currentRating: res.data.score ? res.data.score : 0
                })
            }
        )
    }

    commentsList(){
        if(this.state.meeting.status === "FINISHED" || this.state.meeting.status === "IN_PROGRESS") {
            return <div>
                <CommentsList comments={this.state.comments} onCommentChange={this.addComment.bind(this)}/>
                <SockJsClient
                    url={`https://meetonapi.herokuapp.com/ws`}
                    topics={[`/meeting/${this.state.meeting.meetingId}/queue/comments`]}
                    onMessage={(comment) => this.handleComment(comment)}
                    ref={ (client) => { this.clientRef = client }}/>
            </div>
        }
    }

    addComment(content){
        const comment = {
            meeting_id: this.state.meeting.meetingId,
            meetingName: this.state.meeting.name,
            user_id: this.state.currentUser.id,
            username: this.state.currentUser.username,
            content: content
        }
        this.clientRef.sendMessage("/app/createComment", JSON.stringify(comment));
    }

    handleComment(comment){
        console.log(comment)
        this.state.comments.push(comment)
        this.setState({
            comments: this.state.comments
        })
    }

    deleteMeeting() {
        MeetingService.deleteMeeting(this.props.match.params.id).then((res) => {
            this.props.history.push('/meetings');
        });
    }

    buttonDelete() {
        if ((this.state.meeting.managerId === AuthService.getCurrentUser()?.id)&&(this.state.meeting.status!=="FINISHED"))
            return  <div>
                        <button className="btn btn-danger" style={{marginLeft:"5px"}} onClick={this.deleteMeeting.bind(this)}>Delete</button>
                    </div>
    }

    buttonUpdate() {
        if ((this.state.meeting.managerId === AuthService.getCurrentUser()?.id)&&(this.state.meeting.status!=="FINISHED"))
            return <div>
                        <Link to={`/update/${this.props.match.params.id}`}>
                            <button className="btn btn-primary">Update</button>
                        </Link>
                    </div>
    }

    handleEnroll() {
        if(this.state.meeting.isPrivate)
            this.props.history.push(`/enroll/${this.props.match.params.id}`)
        else
            this.sendEnrollment();
    }

    sendEnrollment() {
        let request = {
            user_id: this.state.currentUser.id,
            meeting_id:     this.state.meeting.meetingId
        };
        console.log(request)
        RequestService.createRequest(request).then(res => {
            this.props.history.push('/meetings');
        });
    }

    buttonEnroll() {
        if (AuthService.getCurrentUser()&&(this.state.meeting.managerId !== AuthService.getCurrentUser()?.id)&&(this.state.meeting.status!=="FINISHED")){
            if(this.state.request)
                return  <div>
                            <p>You have already enrolled to the meeting!</p>
                            <p>Status: {this.state.request.status}</p>
                        </div>
            if(this.state.meeting.isParticipantAmountRestricted && this.state.requestsAmount >= this.state.meeting.participantAmount)
                return <div><p>No available places!</p></div>

            return  <div>
                            <button className="btn btn-primary" style={{marginLeft:"5px"}} onClick={this.handleEnroll.bind(this)}>Enroll</button>
                    </div>
        }
    }

    platformList() {
        if (this.state.request?.status === "APPROVED") {
            return  (
                <div>
                    <div className="row">Platforms </div>
                    {
                        this.state.meeting.meetingPlatforms?.map(
                            platform =>
                                <div className="row mb-2" style={{ background: "#dadada", borderRadius: "10px" }}>
                                    <div className="col-2"><img width="20px" height="20px" src="https://computercraft.ru/uploads/monthly_2018_09/discord_logo.0.jpg.7a69ad4c741ee1fb1bd39758714e7da5.jpg"></img></div>
                                    <div className="col-3">{platform.name}</div>
                                    <div className="col-6">{platform.address}</div>
                                </div>
                        )
                    }
                </div>
            );
        }
    }

    handleRatingChange = (value) => {
        const score={
            user_id: this.state.currentUser.id,
            score: parseInt(value)
        }
        ScoreService.setScore(this.state.meeting.meetingId, score).then( () =>
            this.updateMeetingRating()
        )
    }

    meetingRating = () => {
        console.log(this.state.currentUser);
        if(!(this.state.meeting.managerId === this.state.currentUser?.id )) {
            return <MeetingRating onRatingChange={this.handleRatingChange.bind(this)} rating={this.state.userRating}/>
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
                                    <Box component="fieldset" mb={3} borderColor="transparent">
                                        <Rating name="read-only" size="large" value={this.state.currentRating} precision={0.1} readOnly />
                                    </Box>
                                </div>
                                {this.meetingRating()}
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
                                {this.platformList()}

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