import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return {
    +         /* Not implemented: cannot create static file insight from UI */
    +     };
    + };
    + 
    + export default TestComponent;