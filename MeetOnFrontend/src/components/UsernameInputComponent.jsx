import React, { Component } from 'react';
import Form from "react-validation/build/form";
import CheckButton from "react-validation/build/button";
import AuthService from "../services/AuthService";
import TextField from '@material-ui/core/TextField';
import RegistrationStepperComponent from './RegistrationStepperComponent';

class UsernameInputComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            email: '',
            username:'',
            firstName: '',
            secondName: '',
            accessToken: ''
        }
        this.handleLogin = this.handleLogin.bind(this);
        this.onChangeUsername = this.onChangeUsername.bind(this);
    }
    
    componentDidMount() {
        const params = new URLSearchParams (this.props.location.search);

        this.state.email = params.get('email')
        this.state.firstName = params.get('firstName')
        this.state.secondName = params.get('secondName')
        this.state.accessToken = params.get('accessToken')
        this.setState({
            email: this.state.email,
            firstName: this.state.firstName,
            secondName: this.state.secondName,
            accessToken: this.state.accessToken
        })
    }

    onChangeUsername(e) {
        this.setState({
            username: e.target.value
        });
    }

    handleLogin(e) {
        e.preventDefault();

        this.setState({
            message: "",
            loading: true
        });

        this.form.validateAll();

        let user = {
            email: this.state.email,
            username: this.state.username,
            firstName: this.state.firstName,
            secondName: this.state.secondName,
            accessToken: this.state.accessToken
        }

        if (this.checkBtn.context._errors.length === 0) {
            AuthService.loginViaGoogle(user).then(
                () => {
                    this.props.history.push("/profile");
                    window.location.reload();
                },
                error => {
                    const resMessage =
                        (error.response &&
                            error.response.data &&
                            error.response.data.message) ||
                        error.message ||
                        error.toString();
    
                    this.setState({
                        loading: false,
                        message: resMessage
                    });
                }
            );
        } else {
            this.setState({
                loading: false
            });
        }
    }

    render() {
        const styles = {
            helper: {
                 color: '#CD5642',
            }
        }
        return (
            <div className="container">
                <div className="row">
                    <div className="col-6 offset-3">
                        <div className="row mt-1 mb-3">
                            <div className="col">
                                <p className="text-center raleway custom-paragraph">Just one more step to finish your registration...</p>
                                <RegistrationStepperComponent/>
                            </div>
                        </div>
                        <Form onSubmit={this.handleLogin} ref={c => { this.form = c; }}>
                        <div className="row">
                                <div className="col">
                                    <p className="raleway custom-paragraph" style={{marginLeft: "9px", marginBottom: "0"}}>Username</p>
                                    <TextField style={{ margin: "8px" }} placeholder="Enter your username" required
                                        fullWidth margin="normal" variant="outlined" onChange={this.onChangeUsername}
                                        InputLabelProps={{ shrink: true }}  error={this.state.message} helperText={this.state.message}
                                        FormHelperTextProps={{ style: styles.helper }} />
                                </div>
                            </div>

                            <div className="row mt-3 mb-2">
                                <div className="col">
                                    <button className="btn btn-primary raleway custom-paragraph" style={{width: "100%", backgroundColor: "#373737", marginLeft: "8px", marginRight: "-8px"}} type="submit">Finish</button>
                                </div>
                            </div>
                            <CheckButton style={{ display: "none" }} ref={c => { this.checkBtn = c; }} />
                        </Form>
                    </div>
                </div>
            </div>
        );
    }
}

export default UsernameInputComponent;