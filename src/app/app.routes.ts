import { Routes } from '@angular/router';
import {LoginFormComponent} from "./login/login-form/login-form.component";
import {ChatPageComponent} from "./chat/chat-page/chat-page.component";
import {LoginPageComponent} from "./login/login-page/login-page.component";

export const routes: Routes = [
  {path : "", component: LoginPageComponent},
  {path : "chat", component: ChatPageComponent}
];
