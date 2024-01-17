import {Component, OnDestroy, OnInit} from "@angular/core";
import {Subscription} from "rxjs";
import {AuthenticationService} from "../../login/authentication.service";
import {Message} from "../message.model";
import {MessagesService} from "../messages.service";
import {FormBuilder, ReactiveFormsModule} from "@angular/forms";
import {AsyncPipe, DatePipe, NgForOf, NgIf} from "@angular/common";
import {LoginService} from '../../login/login.service';
import {MessagesComponent} from "../messages/messages.component";
import {NewMsgFormComponent} from "../new-msg-form/new-msg-form.component";

@Component({
  selector: "app-chat-page",
  templateUrl: "./chat-page.component.html",
  styleUrls: ["./chat-page.component.css"],
  imports: [
    DatePipe,
    ReactiveFormsModule,
    AsyncPipe,
    NgForOf,
    NgIf,
    MessagesComponent,
    NewMsgFormComponent
  ],
  standalone: true
})
export class ChatPageComponent implements OnInit, OnDestroy {
  mensaje = this.messagesService.getMessages();
  username$ = this.authenticationService.getUsername();
  messages: Message[] = [];
  username: string | null = null;
  usernameSubscription: Subscription;

  constructor(
    private messagesService: MessagesService,
    private loginService: LoginService,
    private authenticationService: AuthenticationService,
  ) {
    this.usernameSubscription = this.username$.subscribe((u) => {
      this.username = u;
    });

  }

  ngOnDestroy(): void {
    throw new Error("Method not implemented.");
  }

  ngOnInit(): void {
  }

  onLogout() {
    this.loginService.logout()
  }

  onSendMsg(msgTxt: string ) {
    if (this.username) {
      this.messagesService.postMessage({
        text: msgTxt,
        username: this.username,
        timestamp: Date.now(),
      });
      this.mensaje.subscribe(m => this.messages = m);
    }
  }
}
