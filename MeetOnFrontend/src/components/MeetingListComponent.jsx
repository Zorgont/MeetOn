import React, { Component } from "react";
import { Link } from 'react-router-dom';
import MeetingService from '../services/MeetingService';
import TagService from '../services/TagService';
import AuthService from "../services/AuthService";
import Switch from '@material-ui/core/Switch';
import FormGroup from '@material-ui/core/FormGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import {Pagination} from "@material-ui/lab";
import MeetingGroupComponent from "./MeetingGroupComponent";

class MeetingList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            meetings: [],
            tags: [],
            selectedTags: [],
            currentUser: AuthService.getCurrentUser(),
            page: 1,
            pagesNumber: 5
        }
    }
    
    componentDidMount() {
        let script = document.createElement("script");
        script.src = "https://code.jquery.com/jquery-3.1.0.js";
        script.async = true;
        document.body.appendChild(script);

        script = document.createElement("script");
        script.src = "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js";
        script.async = true;
        document.body.appendChild(script);

        this.getListByTags()
        MeetingService.getPagesNumberByTags().then((res) => {
            this.setState({
                pagesNumber: res.data
            })
        })
        TagService.getTags().then((res) => {
            this.setState({tags: res.data});
        });
    }

    onTagChecked(tag, event) {
        let isChecked = event.target.checked;
        console.log("checked? " + isChecked);

        let array = this.state.selectedTags;
        if (isChecked && !array.includes(tag))
            array.push(tag);
        else if (!isChecked && array.includes(tag))
            array.splice(array.indexOf(tag), 1);

        MeetingService.getPagesNumberByTags(this.state.selectedTags).then((res) => {
            this.setState({
                pagesNumber: res.data
            })
        })

        this.getListByTags()
        this.setState({selectedTags: array});
    }

    getListByTags(){
        if(this.state.currentUser)
            MeetingService.getRecommendedMeetingsByTags(this.state.selectedTags, this.state.page).then((res) => {
                this.setState({meetings: res.data});
                console.log(this.state.meetings);
            });
        else
            MeetingService.getMeetingsByTags(this.state.selectedTags,  this.state.page).then((res) => {
                this.setState({meetings: res.data});
                console.log(this.state.meetings);
            });

    }
    changePage(event, newPage){
        this.state.page = newPage;
        this.getListByTags()
        this.setState({
            page: this.state.page
        })
    }

    render() {
        return (
            <div className="container">
                <div className="row">
                    <div className="col">
                        <h2 className="text-center">Favorite meetings</h2>
                    </div>
                </div>
                <div className="row">
                    <div className="col"/>
                    <div className="col">
                        <MeetingGroupComponent meetings={this.state.meetings}/>
                    </div>
                    <div className="col"/>
                </div>
                <div className="row">
                    <div className="col-9">
                        <h2 className="text-center">Meetings list</h2>
                        <table className="table table-striped table-bordered">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>About</th>
                                    <th>Author name</th>
                                    <th>Tags</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    this.state.meetings.map(
                                        meeting => 
                                        <tr key={meeting.id}>
                                            <td><Link to={`/meetings/${meeting.meetingId}`}>{meeting.name}</Link></td>
                                            <td> {meeting.about} </td>
                                            <td> <Link to={`/users/${meeting.managerId}`}>{meeting.managerUsername} </Link></td>
                                            <td>
                                            {
                                                meeting.tags.map(
                                                    tag => 
                                                        <div style={{ margin: "0 10px 10px 0", background: "#ddd", padding: 3, borderRadius: "10px"}}> {tag}</div>
                                                )
                                            }
                                            </td>
                                        </tr>
                                    )
                                }
                            </tbody>
                        </table>
                        <div className="row justify-content-center" >
                            <Pagination count={this.state.pagesNumber} color="primary" hideNextButton={this.state.hideNextButton} onChange={this.changePage.bind(this)} />
                        </div>
                    </div>
                    <div className="col-3">
                        <h2 className="text-center">Filtration</h2>
                        <FormGroup>
                        {
                            this.state.tags.map(tag =>
                            <FormControlLabel
                            control={<Switch id={tag} size="small"  onChange={this.onTagChecked.bind(this, tag)} />}
                            label={tag}
                            />
                            )
                        }
                            </FormGroup>
                    </div>
                </div>
            </div>
        );
    }
}

export default MeetingList;