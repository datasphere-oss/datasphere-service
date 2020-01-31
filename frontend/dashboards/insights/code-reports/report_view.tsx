import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return [
    +         <div key=\"child-0\" block-api-error=\"\" />,
    + 
    +         <iframe
    +             key=\"child-2\"
    +             className=\"webapp-container\"
    +             sandbox=\"allow-forms allow-pointer-lock allow-popups allow-scripts\"
    +             src={iFrameUrl}
    +             frameBorder={0}
    +             width=\"100%\"
    +             height=\"100%\">{`
    + `}</iframe>
    +     ];
    + };
    + 
    + export default TestComponent;