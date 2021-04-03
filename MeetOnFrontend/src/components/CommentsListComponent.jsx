import React, {Component} from "react";

export default class CommentsList extends Component{
    constructor(props) {
        super(props);
        this.state={
            content:""
        }
    }
    SaveComment=(content)=>{
        this.props.onCommentChange(content);
        this.setState({
            content:""
        })
    }
    ChangeContent=(event)=>{
        this.setState({
            content:event.target.value
        })
    }
    render(){

        return (
            <div>
                {!this.props.comments.isEmpty?this.props.comments.map(
                comment =>
                    <div className="container">
                    <div className="row">
                      <div className="col-6" >
                         {comment.username}
                      </div>
                        <div className="col-6" >
                            {comment.date}
                     </div>
                    </div>
                    <div className="row">
                        {comment.content}
                    </div>
                    </div>
                ):<div />
                }

                <div className="row">
                    <label htmlFor="add_comment"> Add comment </label>
                    <input type="text"  name="add_comment" className="form-control" value={this.state.content} onChange={this.ChangeContent.bind(this)} />
                </div>
                <div className="row">
                    <button className="btn btn-success" onClick={this.SaveComment.bind(this,this.state.content)}>Post</button>
                </div>
            </div>
        )
    }
}


