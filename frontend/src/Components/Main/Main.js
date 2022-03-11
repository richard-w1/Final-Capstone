import React from 'react';
import { Component } from 'react'
import { Switch, Route, Redirect, Link } from 'react-router-dom'
import Login from '../Login/Login'
import Register from '../Register/Register'
import Home from '../Home/Home'
import SearchForm from '../SearchForm/SearchForm'
import ViewGroup from '../ViewGroup/ViewGroup'
import CreateGroup from '../CreateGroup/CreateGroup'
import Splash from '../Splash/Splash';
import { addToken, deleteUser } from '../../Redux/actionCreators'
import { connect } from 'react-redux'
import { withRouter } from 'react-router-dom'


const mapStateToProps = state => {
    return {
        token: state.token,
        user: state.user
    }
}

const mapDispatchToProps = (dispatch) => ({
    addToken: () => { dispatch(addToken()) },
    deleteUser: () => { dispatch(deleteUser()) }
});

class Main extends Component {
    constructor(props) {
        super(props);
    }

    handleLogout = () => {
        this.props.addToken("")
        this.props.deleteUser()
    }

    render() {
        return (

            <div>
                {this.props.token.token !== undefined ?
                        <div>
                            <Link to='/home'>Home | </Link>
                            <Link to='/login' onClick={this.handleLogout}>Logout</Link>
                            <Redirect to='/home' />
                        </div> :
                <Link to='/login'>Home | </Link>
                }
                <Switch>
                    <Route path='/login' component={() => <Login />} />
                    <Route path='/register' component={() => <Register />} />
                    <Route path='/home' component={this.props.token.token !== undefined ? () => <Home token={this.props.token.token} /> : null} />
                    <Route path='/SearchForm' component={() => <SearchForm />} />
                    <Route path='/CreateGroup' component={() => <CreateGroup token={this.props.token.token} />} />
                    <Route path='/ViewGroup' component={() => <ViewGroup token={this.props.token.token} />} />
                    <Route path='/Splash' component={() => <Splash />} />
                    <Redirect to='/Splash' />
                </Switch>
            </div>
        )
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Main));