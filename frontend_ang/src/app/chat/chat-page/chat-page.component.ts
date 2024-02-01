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
import {Router} from '@angular/router';
import {WebsocketService} from "../../app.WebsocketService";

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
	mensaje$ = this.messagesService.getMessages();
	username$ = this.loginService.getUsername();
	messages: Message[] = [];
	username: string | null = null;
	usernameSubscription: Subscription;
	mensajeSubscripton: Subscription;

	constructor(
		private messagesService: MessagesService,
		private loginService: LoginService,
		private authenticationService: AuthenticationService,
		private router: Router,
	) {
		this.usernameSubscription = this.username$.subscribe((u) => {
			this.username = u;
		});
		this.mensajeSubscripton = this.mensaje$.subscribe(m => {
			this.messages = m;
		})
	}

	ngOnDestroy(): void {
		if (this.usernameSubscription) {
			this.usernameSubscription.unsubscribe()
		}
		if (this.mensajeSubscripton) {
			this.mensajeSubscripton.unsubscribe()
		}
	}

	ngOnInit(): void {
		this.messagesService.refreshMessages();
	}

	onLogout() {
		this.loginService.logout()
		this.router.navigate(['/'])
	}

	onSendMsg(msgTxt: string) {
		if (this.username) {
			this.messagesService.postMessage({
				id: null,
				text: msgTxt,
				username: this.username,
				timestamp: Date.now(),
			});
			this.mensaje$.subscribe(m => this.messages = m);
		}
	}
}
