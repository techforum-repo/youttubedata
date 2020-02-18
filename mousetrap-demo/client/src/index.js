import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';

import Mousetrap from 'mousetrap'

// single keys
Mousetrap.bind('1', function() {console.log('you pressed 1')})

// combinations
Mousetrap.bind('ctrl+shift+k', function() { console.log('ctrl shift k'); });

// map multiple combinations to the same callback
Mousetrap.bind(['shift+k', 'ctrl+k'], function() {   console.log('shift k or control k'); });

// gmail style sequences
Mousetrap.bind('g i', function() { console.log('go to inbox'); });
Mousetrap.bind('* a', function() { console.log('select all'); });

Mousetrap.bind('a', function() { console.log('You pressed a and let it come up')},'keyup')

ReactDOM.render(<App />, document.getElementById('root'));

