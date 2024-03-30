import {provideRouter, Routes} from '@angular/router';
import {LoginFormComponent} from "./login/login-form/login-form.component";
import {ChatPageComponent} from "./chat/chat-page/chat-page.component";
import {LoginPageComponent} from "./login/login-page/login-page.component";
import {loginPageGuard} from "./guards/login-page.guard";
import {chatGuard} from "./guards/chat.guard";

export const routes: Routes = [
	{path: "", component: LoginPageComponent, canActivate: [loginPageGuard]},
	{path: "chat", component: ChatPageComponent, canActivate: [chatGuard]},
	{path: "**", redirectTo: "chat"} // Wildcard route for a 404 page
];
